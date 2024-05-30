package tr.edu.ku.comp302.domain.lanceofdestiny;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.chrono.Chronometer;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.GameState;
import tr.edu.ku.comp302.ui.panel.LevelPanel;

import java.awt.*;
import java.util.List;

public class LanceOfDestiny implements Runnable {
    private static int screenWidth = 1280;
    private static int screenHeight = 800;
    private static GameState currentGameState;
    private final Logger logger = LogManager.getLogger(LanceOfDestiny.class);
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private final LevelPanel levelPanel;  // TODO: change this when we implement more than one level
    private final LevelHandler levelHandler;
    private double deltaUpdate = 0.0;
    private double deltaFrame = 0.0;
    private long updates = 0L;
    private long frames = 0L;
    private Character lastMoving;
    private boolean tapMoving;
    private Chronometer chronometer;

    public LanceOfDestiny(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        levelHandler = levelPanel.getLevelHandler();
        levelPanel.requestFocusInWindow();
        currentGameState = GameState.NULL_STATE;
        lastMoving = null;
        tapMoving = false;
        chronometer = new Chronometer();
        startGameLoop();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setCurrentGameState(GameState gameState) {
        currentGameState = gameState;
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        // TODO: Change while loop condition
        while (true) {
            if (levelHandler.getLevel() != null && currentGameState.isPlaying()) {
                long currentTime = chronometer.getCurrentTime();
                if (chronometer.getPreviousTime() == 0) {
                    chronometer.setPreviousTime(currentTime);
                }
                handlePause();
                deltaUpdate += (currentTime - chronometer.getPreviousTime()) / timePerUpdate;
                deltaFrame += (currentTime - chronometer.getPreviousTime()) / timePerFrame;
                update(currentTime);
                chronometer.setPreviousTime(currentTime);
            } else {  // TODO: Change this else statement whenever implemented other game states.
                try {
                    if (chronometer.getPauseStartTime() == null && currentGameState.isPaused()) {
                        chronometer.setPauseStartTime(chronometer.getCurrentTime());
                    }
                    Thread.sleep(1000 / UPS_SET);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void handlePause() {
        if (chronometer.getPauseStartTime() != null){
            chronometer.addPauseTime(chronometer.getCurrentTime() - chronometer.getPauseStartTime());
        }
    }

    private void update(long currentTime) {
        boolean recall = false;
        if (deltaUpdate >= 1) {
            handleGameLogic(currentTime);
            updates++;
            deltaUpdate--;
            recall = true;
        }
        if (deltaFrame >= 1) {
            render();
            levelPanel.repaint();
            frames++;
            deltaFrame--;
            recall = true;
        }
        if (recall) {
            update(currentTime);
        }
    }

    private void handleInputs(){

    }

    private void handleGameLogic(long currentTime) {
        Lance lance = levelHandler.getLance();
        boolean moveLeft = KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed;
        boolean moveRight = KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed;
        double holdSpeed = lance.getSpeedWithHold();
        double tapSpeed = lance.getSpeedWithTap();
        boolean rotateCCW = KeyboardHandler.buttonAPressed && !KeyboardHandler.buttonDPressed;
        boolean rotateCW = KeyboardHandler.buttonDPressed && !KeyboardHandler.buttonAPressed;
        handleLanceMovement(moveLeft, moveRight,tapSpeed, holdSpeed, chronometer);
        handleRotationLogic(rotateCCW, -Lance.rotationSpeed);
        handleRotationLogic(rotateCW, Lance.rotationSpeed);
        handleSteadyStateLogic(!rotateCCW && !rotateCW, Lance.horizontalRecoverySpeed);

        handleFireballLogic();

        handleBarriersMovement(currentTime);

        handleCollisionLogic(currentTime);
    }

    private void render() {
        int width = (int) levelPanel.getSize().getWidth();
        int height = (int) levelPanel.getSize().getHeight();
        if (screenWidth != width || screenHeight != height) {
            levelPanel.setPanelSize(new Dimension(width, height));
            screenWidth = width;
            screenHeight = height;

        }
    }

    // Warning: DO NOT try to make this method clean. You will most likely fail.
    // Let's spend our time in more valuable stuff like writing actually useful code
    // instead of trying to invent key tap event in Swing.
    // Just check for bugs and leave it be.
    // Copilot's thoughts about this function: "I'm not sure what you're trying to do here."
    // (It couldn't even suggest any reasonable code for this)
    private void handleLanceMovement(boolean leftPressed, boolean rightPressed, double tapSpeed, double holdSpeed, Chronometer chronometer) {
        Lance lance = levelHandler.getLance();
        long currentTime = chronometer.getCurrentTime();
        if (leftPressed != rightPressed) {
            int index = leftPressed ? 1 : 0;
            Character oldLastMoving = lastMoving;
            lastMoving = leftPressed ? 'l' : 'r';
            lance.setDirection(leftPressed ? -1 : 1);

            if (tapMoving) {
                tapMoving = false;
                chronometer.resetArrowKeyPressTimes();
                chronometer.resetLanceMovementRemainders();
            }

            if (oldLastMoving == null || !oldLastMoving.equals(lastMoving)) {
                chronometer.setLastMovingTime(currentTime);
            }

            if (chronometer.getArrowKeyPressTime(index) == 0) {
                chronometer.setArrowKeyPressTime(index, currentTime);
            }

            double elapsedTime = currentTime -  chronometer.getArrowKeyPressTimes()[index];
            double elapsedMs = elapsedTime / 1_000_000.0 + chronometer.getLanceMovementRemainder(index);
            double speed = (currentTime - chronometer.getLastMovingTime()) / 1_000_000.0 >= 50 ? holdSpeed : tapSpeed;
            int minPx = calculateMinIntegerPxMovement(speed);
            double minMsToMove = minPx * 1000.0 / speed;

            if (elapsedMs >= minMsToMove) {
                lance.updateXPosition(minPx);
                chronometer.setLanceMovementRemainder(index, elapsedMs - minMsToMove);
                chronometer.setArrowKeyPressTime(index, currentTime);
            }
        } else {
            if (lastMoving != null) {
                int index = lastMoving == 'l' ? 1 : 0;
                if (!tapMoving) {
                    tapMoving = true;
                    chronometer.setLanceMovementRemainder(index, 0);
                    chronometer.setArrowKeyPressTime(index, currentTime);
                }
                double tapTime = (currentTime - chronometer.getLastMovingTime()) / 1_000_000.0;
                if (tapTime >= 500) {
                    lance.setDirection(0);
                    tapMoving = false;
                    lastMoving = null;
                    chronometer.setLastMovingTime(0L);
                    chronometer.resetLanceMovementRemainders();
                    chronometer.resetArrowKeyPressTimes();
                } else {
                    double elapsedTime = currentTime - chronometer.getArrowKeyPressTime(index);
                    double elapsedMs = elapsedTime / 1_000_000.0 + chronometer.getLanceMovementRemainder(index);
                    int minPx = calculateMinIntegerPxMovement(tapSpeed);
                    double minMsToMove = minPx * 1000. / tapSpeed;
                    if (elapsedMs >= minMsToMove) {
                        lance.updateXPosition(minPx);
                        chronometer.setLanceMovementRemainder(index, elapsedMs - minMsToMove);
                        chronometer.setArrowKeyPressTime(index, currentTime);
                    }
                }
            }
        }
    }

    private void handleRotationLogic(boolean keyPressed, double angularSpeed) {
        Lance lance = levelHandler.getLance();
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            lance.incrementRotationAngle(angularChange);
        }
    }

    private void handleSteadyStateLogic(boolean keyPressed, double angularSpeed) {
        Lance lance = levelHandler.getLance();
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            lance.returnToHorizontalState(angularChange);
        }
    }

    private void handleFireballLogic() {
        FireBall fb = levelHandler.getFireBall();
        if (!fb.isMoving()) {
            if (KeyboardHandler.buttonWPressed) {
                fb.launchFireball();
            } else {
                fb.stickToLance(levelHandler.getLance());
            }
        }
        // FIXME assumes this is called UPS_SET times per second
        fb.move(fb.getDx() / UPS_SET, fb.getDy() / UPS_SET);
    }

    private void handleCollisionLogic(long currentTime) {
        Lance lance = levelHandler.getLance();
        if (lance.canCollide(currentTime)) {
            if (CollisionHandler.checkFireBallEntityCollisions(levelHandler.getFireBall(), lance)) {
                lance.setLastCollisionTimeInMillis(currentTime / 1e6);
            }
        }

        CollisionHandler.checkFireBallBorderCollisions(levelHandler.getFireBall(), screenWidth, screenHeight);

        List<Barrier> barriers = levelHandler.getBarriers();
        CollisionHandler.checkFireballBarriersCollisions(levelHandler.getFireBall(), barriers);

        for (Barrier barrier : barriers.stream().toList()) {
            if (barrier.isDead()) {
                if (barrier instanceof ExplosiveBarrier b) {
                    b.dropRemains();
                }
                barriers.remove(barrier);
            }
        }
    }

    private void handleBarriersMovement(long currentTime) {
        for (Barrier barrier : levelHandler.getBarriers()) {
            if (barrier.getMovementStrategy() != null) {
                // If the barrier moved with 0.2 probability and stopped with 0.8, the barriers would stop a lot
                // with 1s intervals. However, if they moved endlessly after rolling <= 0.2, a lot of them would
                // start to move at the same time. So, I decided to increase testing time to 3s and also added
                // stopping with 0.2 probability for moving barriers. This is of course subject to change.
                if (!barrier.isMoving() && currentTime - barrier.getLastDiceRollTime() >= 3_000_000_000L) {
                    barrier.tryMove(currentTime);
                } else if (barrier.isMoving() && currentTime - barrier.getLastDiceRollTime() >= 3_000_000_000L) {
                    barrier.tryStop(currentTime);
                }
                barrier.handleCloseCalls(levelHandler.getBarriers());
                barrier.move(barrier.getSpeed() / UPS_SET);
            }
        }

        List<Remain> remains = levelHandler.getRemains();
        for (Remain remain : remains.stream().filter(Remain::isDropped).toList()) {
            remain.move(remain.getSpeed() / UPS_SET);
            if (remain.getYPosition() > screenHeight) {
                remains.remove(remain);
            }
            //TODO: handle collision with lance
        }
    }

    private double calculateAngularChangePerUpdate(double angularSpeed) {
        return angularSpeed * getMsPerUpdate() / 1000.0;
    }

    private int calculateMinIntegerPxMovement(double speed) {
        double speedInMs = speed / 1000.0;
        double onePxMs = 1.0 / speedInMs;
        if (onePxMs >= getMsPerUpdate()) {
            return 1;
        } else {
            double pxPerUpdate = getMsPerUpdate() / onePxMs;
            return ((int) pxPerUpdate) + 1;
        }
    }

    private double getMsPerUpdate() {
        return 1000.0 / UPS_SET;
    }

    private void startGameLoop() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public static void setScreenWidth(int screenWidth) {
        LanceOfDestiny.screenWidth = screenWidth;
    }

    public static void setScreenHeight(int screenHeight) {
        LanceOfDestiny.screenHeight = screenHeight;
    }
}

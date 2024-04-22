package tr.edu.ku.comp302.domain.lanceofdestiny;


import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.LevelPanel;

import java.awt.*;


public class LanceOfDestiny implements Runnable {
    private MainFrame mainFrame;
    private LevelPanel levelPanel;  // TODO: change this when we implement more than one level
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private static int screenWidth = 1280;
    private static int screenHeight = 800;
    private double deltaUpdate = 0.0;
    private double deltaFrame = 0.0;
    private long updates = 0L;
    private long frames = 0L;
    private GameState currentGameState;
    private Thread gameThread;
    private Character lastMoving;
    private long lastMovingTime;
    private long previousTime;
    private long[] arrowKeyPressTimes = new long[2];    // [rightArrowKeyPressTime, leftArrowKeyPressTime]
    private double[] lanceMovementRemainder = new double[2];   // [remainderTotalRightArrowKey, remainderTotalLeftArrowKey]
    private boolean tapMoving;
    private final Lance lance;  // TODO: Later change this.

    public LanceOfDestiny(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        lance = levelPanel.getLanceView().getLance();
        mainFrame = new MainFrame(levelPanel);
        levelPanel.requestFocusInWindow();
        currentGameState = GameState.PLAYING;   // for testing purposes.
        lastMoving = null;
        lastMovingTime = 0;
        tapMoving = false;
        startGameLoop();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        previousTime = System.nanoTime();
        // TODO: Change while loop condition
        while (true) {
            if (currentGameState.isPlaying()){
                long currentTime = System.nanoTime();
                deltaUpdate += (currentTime - previousTime) / timePerUpdate;
                deltaFrame += (currentTime - previousTime) / timePerFrame;
                update(currentTime);
                previousTime = currentTime;
            }else{  // TODO: Change this else statement whenever implemented other game states.
                try{
                    Thread.sleep(1000 / UPS_SET);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    private void update(long currentTime){
        while (deltaUpdate >= 1){
            handleGameLogic(currentTime);
            updates++;
            deltaUpdate--;
        }
        while (deltaFrame >= 1){
            render();
            levelPanel.repaint();
            frames++;
            deltaFrame--;
        }
    }

    private void render(){
        int width = (int) levelPanel.getSize().getWidth();
        int height = (int) levelPanel.getSize().getHeight();
        if (screenWidth != width || screenHeight != height){
            levelPanel.setPanelSize(new Dimension(width, height));
            screenWidth = width;
            screenHeight = height;
        }
        System.out.println(levelPanel.getLanceView().getLance().getLength() + ", " + levelPanel.getSize());
    }

    private void handleGameLogic(long currentTime){
        boolean moveLeft = KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed;
        boolean moveRight = KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed;
        double holdSpeed = lance.getSpeedWithHold();
        double tapSpeed = lance.getSpeedWithTap();
        handleLanceMovement(moveLeft, moveRight, arrowKeyPressTimes, currentTime, tapSpeed, holdSpeed, lanceMovementRemainder);

        // TODO: check names
        boolean rotateCCW = KeyboardHandler.buttonAPressed && !KeyboardHandler.buttonDPressed;
        boolean rotateCW = KeyboardHandler.buttonDPressed && !KeyboardHandler.buttonAPressed;
        handleRotationLogic(rotateCCW, -Lance.rotationSpeed);
        handleRotationLogic(rotateCW, Lance.rotationSpeed);
        handleSteadyStateLogic(!rotateCCW && !rotateCW, Lance.horizontalRecoverySpeed);
    }

    // Warning: DO NOT try to make this method clean. You will most likely fail.
    // Let's spend our time in more valuable stuff like writing actually useful code
    // instead of trying to invent key tap event in Swing.
    // Just check for bugs and leave it be.
    // Copilot's thoughts about this function: "I'm not sure what you're trying to do here."
    // (It couldn't even suggest any reasonable code for this)
    private void handleLanceMovement(boolean leftPressed, boolean rightPressed, long[] arrowKeyPressTimes, long currentTime, double tapSpeed, double holdSpeed, double[] lanceMovementRemainder) {
        if (leftPressed != rightPressed) {
            int index = leftPressed ? 1 : 0;
            Character oldLastMoving = lastMoving;
            lastMoving = leftPressed ? 'l' : 'r';
            lance.setDirection(leftPressed ? -1 : 1);

            if (tapMoving) {
                tapMoving = false;
                arrowKeyPressTimes[0] = 0;
                arrowKeyPressTimes[1] = 0;
                lanceMovementRemainder[0] = 0;
                lanceMovementRemainder[1] = 0;
            }

            if (oldLastMoving == null || !oldLastMoving.equals(lastMoving)) {
                lastMovingTime = currentTime;
            }

            if (arrowKeyPressTimes[index] == 0) {
                arrowKeyPressTimes[index] = currentTime;
            }

            double elapsedTime = currentTime - arrowKeyPressTimes[index];
            double elapsedMs = elapsedTime / 1_000_000.0 + lanceMovementRemainder[index];
            double speed = (currentTime - lastMovingTime) / 1_000_000.0 >= 50 ? holdSpeed : tapSpeed;
            int minPx = calculateMinIntegerPxMovement(speed);
            double minMsToMove = minPx * 1000.0 / speed;

            if (elapsedMs >= minMsToMove) {
                lance.updateXPosition(minPx);
                lanceMovementRemainder[index] = elapsedMs - minMsToMove;
                arrowKeyPressTimes[index] = System.nanoTime();
            }
        } else {
            if (lastMoving != null) {
                int index = lastMoving == 'l' ? 1 : 0;
                if (!tapMoving) {
                    tapMoving = true;
                    lanceMovementRemainder[index] = 0;
                    arrowKeyPressTimes[index] = currentTime;
                }
                double tapTime = (currentTime - lastMovingTime) / 1_000_000.0;
                if (tapTime >= 500) {
                    lance.setDirection(0);
                    tapMoving = false;
                    lastMoving = null;
                    lastMovingTime = 0;
                    lanceMovementRemainder[index] = 0;
                    arrowKeyPressTimes[index] = 0;
                } else {
                    double elapsedTime = currentTime - arrowKeyPressTimes[index];
                    double elapsedMs = elapsedTime / 1_000_000.0 + lanceMovementRemainder[index];
                    int minPx = calculateMinIntegerPxMovement(tapSpeed);
                    double minMsToMove = minPx * 1000. / tapSpeed;
                    if (elapsedMs >= minMsToMove) {
                        lance.updateXPosition(minPx);
                        lanceMovementRemainder[index] = elapsedMs - minMsToMove;
                        arrowKeyPressTimes[index] = System.nanoTime();
                    }
                }
            }
        }
    }

    private void handleRotationLogic(boolean keyPressed, double angularSpeed) {
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            lance.incrementRotationAngle(angularChange);
        }
    }

    private void handleSteadyStateLogic(boolean keyPressed, double angularSpeed) {
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            lance.returnToHorizontalState(angularChange);
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
        gameThread = new Thread(this);
        gameThread.start();
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static void setScreenWidth(int screenWidth) {
        LanceOfDestiny.screenWidth = screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static void setScreenHeight(int screenHeight) {
        LanceOfDestiny.screenHeight = screenHeight;
    }
}

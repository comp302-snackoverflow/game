package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.entity.Barriers.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.Remains;

import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.collision.CollisionError;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.RemainsView;


public class LanceOfDestiny implements Runnable{
    private MainFrame mainFrame;
    private LevelPanel levelPanel;  // TODO: change this when we implement more than one level
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Thread gameThread;
    private Character lastMoving;
    private long lastMovingTime;
    private boolean tapMoving;

    public LanceOfDestiny(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        mainFrame = MainFrame.createMainFrame();
        levelPanel.requestFocusInWindow();
        lastMoving = null;
        lastMovingTime = 0;
        tapMoving = false;
        startGameLoop();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;
        long previousTime = System.nanoTime();
        int frames = 0;
        int updates = 0;
        long[] arrowKeyPressTimes = new long[2];    // [rightArrowKeyPressTime, leftArrowKeyPressTime]
        double[] totalArrowKeyPressTimes = new double[2];   // [totalRightArrowKey, totalLeftArrowKey]
        double deltaUpdate = 0.0;
        double deltaFrame = 0.0;
        double holdSpeed = levelPanel.getLanceView().getLance().getSpeedWithHold();
        double tapSpeed = levelPanel.getLanceView().getLance().getSpeedWithTap();
        // TODO: Change while loop condition
        while (true) {
            long currentTime = System.nanoTime();
            deltaUpdate += (currentTime - previousTime) / timePerUpdate;
            deltaFrame += (currentTime - previousTime) / timePerFrame;
            previousTime = currentTime;
            if (deltaUpdate >= 1) {
                boolean moveLeft = KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed;
                boolean moveRight = KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed;

                handleLanceMovement(moveLeft, moveRight, arrowKeyPressTimes,        // I hate my life
                        currentTime, tapSpeed, holdSpeed, totalArrowKeyPressTimes); // Thanks, COMP 302! ☺

                // TODO check names
                boolean rotateCCW = KeyboardHandler.buttonAPressed && !KeyboardHandler.buttonDPressed;
                boolean rotateCW = KeyboardHandler.buttonDPressed && !KeyboardHandler.buttonAPressed;

                handleRotationLogic(rotateCCW, -20.0);
                handleRotationLogic(rotateCW, 20.0);

                handleSteadyStateLogic(!rotateCCW && !rotateCW, 45);

                if (KeyboardHandler.buttonWPressed && levelPanel.getFireBallView().getFireBall().getDy() == 0) {
                    levelPanel.getFireBallView().getFireBall().launchFireball();
                }

                levelPanel.getFireBallView().getFireBall().move();
                CollisionHandler.checkCollisions(levelPanel.getFireBallView(), levelPanel.getLanceView());
                CollisionHandler.checkFireBallBorderCollisions(levelPanel.getFireBallView(), mainFrame.getFrameWidth(), mainFrame.getFrameHeight());
                for (int i = 0; i < levelPanel.getBarrierViews().size(); i++) {
                    try {
                        if (CollisionHandler.testFireballBarrierOverlap(levelPanel.getFireBallView().getFireBall(),levelPanel.getBarrierViews().get(i).getBarrier()) != null){
                            levelPanel.getFireBallView().getFireBall().handleReflection(0);
                            levelPanel.getBarrierViews().get(i).getBarrier().setHealth(levelPanel.getBarrierViews().get(i).getBarrier().getHealth()-1);
                        }
                    } catch (CollisionError e) {
                        throw new RuntimeException(e);
                    }
                }
                //check if barrier is broken or not
                for (int i = 0; i < levelPanel.getBarrierViews().size(); i++) {
                    if (levelPanel.getBarrierViews().get(i).getBarrier().isDead()) {
                        if(levelPanel.getBarrierViews().get(i).getBarrier() instanceof ExplosiveBarrier){
                            Remains remain = ((ExplosiveBarrier)levelPanel.getBarrierViews().get(i).getBarrier()).dropRemains();
                            RemainsView remainView = new RemainsView(remain);
                            levelPanel.getRemainViews().add(remainView);
                        }
                        levelPanel.getBarrierViews().remove(i);
                        break;
                    }
                }
                //barrier movement
                for(BarrierView barrierView : levelPanel.getBarrierViews()){
                    if(barrierView.getBarrier().getMovementStrategy()!= null){
                        barrierView.getBarrier().getMovementStrategy().checkCollision(levelPanel.getBarrierViews());
                        barrierView.getBarrier().move();
                    }
                }

                for (RemainsView remainsView : levelPanel.getRemainViews()) {
                    remainsView.getRemains().move();
                    //TODO: handle collision with lance, and also remove when it goes below the wall.
                }

                updates++;
                deltaUpdate--;
            }

            if (deltaFrame >= 1) {
                levelPanel.repaint();
                frames++;
                deltaFrame--;
            }

        }
    }

    // Warning: DO NOT try to make this method clean. You will most likely fail.
    // Let's spend our time in more valuable stuff like writing actually useful code
    // instead of trying to invent key tap event in Swing.
    // Just check for bugs and leave it be.
    // Copilot's thoughts about this function: "I'm not sure what you're trying to do here."
    // (It couldn't even suggest any reasonable code for this)
    private void handleLanceMovement(boolean leftPressed, boolean rightPressed, long[] keyPressTimes, long currentTime, double tapSpeed, double holdSpeed, double[] remainder) {
        if (leftPressed != rightPressed) {
            int index = leftPressed ? 1 : 0;
            Character oldLastMoving = lastMoving;
            lastMoving = leftPressed ? 'l' : 'r';
            if (tapMoving) {
                tapMoving = false;
                keyPressTimes[0] = 0;
                keyPressTimes[1] = 0;
                remainder[0] = 0;
                remainder[1] = 0;
            }

            if (oldLastMoving == null || !oldLastMoving.equals(lastMoving)) {
                lastMovingTime = currentTime;
            }

            if (keyPressTimes[index] == 0) {
                keyPressTimes[index] = currentTime;
            }

            double elapsedTime = currentTime - keyPressTimes[index];
            double elapsedMs = elapsedTime / 1_000_000.0 + remainder[index];
            double speed = (currentTime - lastMovingTime) / 1_000_000.0 >= 50 ? holdSpeed : tapSpeed;
            int minPx = calculateMinIntegerPxMovement(speed);
            double minMsToMove = minPx * 1000.0 / speed;

            if (elapsedMs >= minMsToMove){
                levelPanel.getLanceView().getLance().updateXPosition(leftPressed ? -minPx : minPx);
                remainder[index] = elapsedMs - minMsToMove;
                keyPressTimes[index] = System.nanoTime();
            }
        } else {
            if (lastMoving != null) {
                int index = lastMoving == 'l' ? 1 : 0;
                if (!tapMoving) {
                    tapMoving = true;
                    remainder[index] = 0;
                    keyPressTimes[index] = currentTime; // used as last moving time I guess
                }
                double tapTime = (currentTime - lastMovingTime) / 1_000_000.0;
                if (tapTime >= 500) {
                    tapMoving = false;
                    lastMoving = null;
                    lastMovingTime = 0;
                    remainder[index] = 0;
                    keyPressTimes[index] = 0;
                } else {
                    double elapsedTime = currentTime - keyPressTimes[index];
                    double elapsedMs = elapsedTime / 1_000_000.0 + remainder[index];
                    int minPx = calculateMinIntegerPxMovement(tapSpeed);
                    double minMsToMove = minPx * 1000. / tapSpeed;
                    if (elapsedMs >= minMsToMove) {
                        levelPanel.getLanceView().getLance().updateXPosition(lastMoving == 'l' ? -minPx : minPx);
                        remainder[index] = elapsedMs - minMsToMove;
                        keyPressTimes[index] = System.nanoTime();
                    }
                }
            }
        }
    }

    private void handleRotationLogic(boolean keyPressed, double angularSpeed) {
        if (keyPressed) {
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            levelPanel.getLanceView().rotateLance(angularChange);
        }
    }

    private void handleSteadyStateLogic(boolean keyPressed, double angularSpeed){
        if (keyPressed){
            double angularChange = calculateAngularChangePerUpdate(angularSpeed);
            levelPanel.getLanceView().getLance().stayInSteadyState(angularChange);
        }
    }

    private double calculateAngularChangePerUpdate(double angularSpeed){
        return angularSpeed * getMsPerUpdate() / 1000.0;
    }

    private int calculateMinIntegerPxMovement(double speed){
        double speedInMs = speed / 1000.0;
        double onePxMs = 1.0 / speedInMs;
        if (onePxMs >= getMsPerUpdate()){
            return 1;
        }else{
            double pxPerUpdate = getMsPerUpdate() / onePxMs;
            return ((int) pxPerUpdate) + 1;
        }
    }

    private double getMsPerUpdate(){
        return 1000.0 / UPS_SET;
    }



    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
}

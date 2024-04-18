package tr.edu.ku.comp302.domain.lanceofdestiny;


import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.LevelPanel;


public class LanceOfDestiny implements Runnable{

    private MainFrame mainFrame;
    private LevelPanel levelPanel;  // TODO: change this when we implement more than one level
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private Thread gameThread;

    public LanceOfDestiny(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        mainFrame = new MainFrame(levelPanel);
        levelPanel.requestFocusInWindow();
        startGameLoop();
    }

    @Override
    public void run() {
        double timePerFrame = 1_000_000_000.0 / FPS_SET;
        double timePerUpdate = 1_000_000_000.0 / UPS_SET;

        long previousTime = System.nanoTime();
        long[] keyPressTimes = new long[2]; // [keyPressTimeA, keyPressTimeD]
        long releaseTime = 0;
        double[] remainderHolder = new double[2];
        double steadyStateTime = 0.0;
        int frames = 0;
        int updates = 0;
        long[] arrowKeyPressTimes = new long[2];    // [rightArrowKeyPressTime, leftArrowKeyPressTime]
        double[] totalArrowKeyPressTimes = new double[2];   // [totalRightArrowKey, totalLeftArrowKey]
        double deltaUpdate = 0.0;
        double deltaFrame = 0.0;
        double speed = levelPanel.getLanceView().getLance().getSpeedWithHold();
        // TODO: Change while loop condition
        while (true){
            long currentTime = System.nanoTime();
            deltaUpdate += (currentTime - previousTime)/ timePerUpdate;
            deltaFrame += (currentTime - previousTime)/ timePerFrame;
            previousTime = currentTime;
            if (deltaUpdate >= 1){
                handleArrowKeyPress(KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed,
                                    arrowKeyPressTimes, 0, currentTime, speed, totalArrowKeyPressTimes);

                handleArrowKeyPress(KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed,
                        arrowKeyPressTimes, 1, currentTime, speed, totalArrowKeyPressTimes);

                // Handle rotation logic for 'A' key
                handleRotationLogic(KeyboardHandler.buttonAPressed, keyPressTimes, currentTime, 0, -0.4, remainderHolder);

                // Handle rotation logic for 'D' key
                handleRotationLogic(KeyboardHandler.buttonDPressed, keyPressTimes, currentTime, 1, 0.4, remainderHolder);


                if (KeyboardHandler.buttonAReleased && KeyboardHandler.buttonDReleased || (KeyboardHandler.buttonAPressed && KeyboardHandler.buttonDPressed)){
                        if (releaseTime == 0){
                            releaseTime = System.nanoTime();
                        }else{
                            long elapsedTime = currentTime - releaseTime;
                            double elapsedReleaseMs = (double) elapsedTime / 1_000_000.0;
                            steadyStateTime += elapsedReleaseMs;
                            if (steadyStateTime >= 10){
                                levelPanel.getLanceView().getLance().stayInSteadyState(0.45);
                                steadyStateTime -= 10;
                                releaseTime = 0;
                            }
                        }
                }else{
                    releaseTime = 0;
                }
                updates++;
                deltaUpdate--;


            }
            if (deltaFrame >= 1){
                levelPanel.repaint();
                frames++;
                deltaFrame--;
            }

        }
    }

    private void handleArrowKeyPress(boolean arrowKeyPressed, long[] keyPressTimes, int index, long currentTime, double speed, double[] remainderHolder) {
        if (arrowKeyPressed) {
            if (keyPressTimes[index] == 0) {
                keyPressTimes[index] = currentTime;
            } else {
                long elapsedTime = currentTime - keyPressTimes[index];
                double elapsedMs = (double) elapsedTime / 1_000_000.0 + remainderHolder[index];
                int minIntPxMovement = calculateMinIntegerPxMovement();
                if (elapsedMs >= minIntPxMovement / speed) {
                    levelPanel.getLanceView().moveLance(minIntPxMovement);
                    keyPressTimes[index] = 0;
                    remainderHolder[index] -= minIntPxMovement / speed;
                }
            }
        } else {
            keyPressTimes[index] = 0;
            remainderHolder[index] = 0;
        }
    }

    private void handleRotationLogic(boolean keyPressed, long[] keyPressTimes, long currentTime, int index, double degrees, double[] remainderHolder) {
        if (keyPressed) {
            if (keyPressTimes[index] == 0) {
                keyPressTimes[index] = currentTime;
            } else {
                long elapsedTime = currentTime - keyPressTimes[index];
                double elapsedMs = (double) elapsedTime / 1_000_000.0 + remainderHolder[index];
                if (elapsedMs >= 20) {
                    levelPanel.getLanceView().rotateLance(degrees);
                    keyPressTimes[index] = 0;
                    remainderHolder[index] = elapsedMs - 20;
                }
            }
        } else {
            keyPressTimes[index] = 0;
            remainderHolder[index] = 0;
        }
    }

    private int calculateMinIntegerPxMovement(){
        double speedInMs = levelPanel.getLanceView().getLance().getSpeedWithHold() / 1000.0;
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

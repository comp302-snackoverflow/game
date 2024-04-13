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
        double total = 0.0;
        double deltaUpdate = 0.0;
        double deltaFrame = 0.0;
        double speed = levelPanel.getLanceView().getLance().getSpeedWithHold();     // TODO: Change when player can resize screen
        // TODO: Change while loop condition
        while (true){
            long currentTime = System.nanoTime();
            deltaUpdate += (currentTime - previousTime)/ timePerUpdate;
            deltaFrame += (currentTime - previousTime)/ timePerFrame;
            total +=  (double)(currentTime - previousTime) / 1_000_000_000.0;

            previousTime = currentTime;
            if (deltaUpdate >= 1){

                if ((KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed) ||
                        (!KeyboardHandler.rightArrowPressed && KeyboardHandler.leftArrowPressed)){
                    if (total >= 1.0/speed){
                        levelPanel.getLanceView().moveLance(1);
                        total -= 1.0/speed;
                    }
                }
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
        }
    }



    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
}

package tr.edu.ku.comp302.domain.lanceofdestiny;

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
            total +=  (currentTime - previousTime) / 1_000_000_000.0;
            previousTime = currentTime;
            if (deltaUpdate >= 1){
                if ((KeyboardHandler.rightArrowPressed && !KeyboardHandler.leftArrowPressed) ||
                        (!KeyboardHandler.rightArrowPressed && KeyboardHandler.leftArrowPressed)){
                    if (total >= 1.0/speed){
                        levelPanel.getLanceView().moveLance(1);
                        total -= 1.0/speed;
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

    private void startGameLoop(){
        gameThread = new Thread(this);
        gameThread.start();
    }
}

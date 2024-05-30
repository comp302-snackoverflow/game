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
                update();
                chronometer.setPreviousTime(currentTime);
            } else {  // TODO: Change this else statement whenever implemented other game states.
                try {

                    if (chronometer.getPauseStartTime() == null && LanceOfDestiny.getCurrentGameState().isPaused()) {
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

    private void update() {
        boolean recall = false;
        if (deltaUpdate >= 1) {
            levelHandler.handleGameLogic(chronometer.getCurrentTime(), chronometer, UPS_SET);
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
            update();
        }
    }

    private void handleInputs(){

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


    private void startGameLoop() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public LevelHandler getLevelHandler(){
        return levelHandler;
    }
    public static void setScreenWidth(int screenWidth) {
        LanceOfDestiny.screenWidth = screenWidth;
    }

    public static void setScreenHeight(int screenHeight) {
        LanceOfDestiny.screenHeight = screenHeight;
    }

    public static GameState getCurrentGameState() {
        return currentGameState;
    }

    public Chronometer getChronometer() {
        return chronometer;
    }

    public LevelPanel getLevelPanel() {
        return levelPanel;
    }

    public int getFPS_SET() {
        return FPS_SET;
    }

    public int getUPS_SET() {
        return UPS_SET;
    }
}

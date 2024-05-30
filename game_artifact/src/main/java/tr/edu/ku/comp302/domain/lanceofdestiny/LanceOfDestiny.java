package tr.edu.ku.comp302.domain.lanceofdestiny;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.chrono.Chronometer;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.GameState;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.PauseSPState;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.PlayingState;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.State;
import tr.edu.ku.comp302.ui.panel.LevelPanel;

public class LanceOfDestiny implements Runnable {
    private static int screenWidth = 1280;
    private static int screenHeight = 800;
    private static GameState currentGameState;
    private State state;
    private final Logger logger = LogManager.getLogger(LanceOfDestiny.class);
    private final int FPS_SET = 120;
    private final int UPS_SET = 200;
    private final LevelPanel levelPanel;  // TODO: change this when we implement more than one level
    private final LevelHandler levelHandler;
    protected double deltaUpdate = 0.0;
    protected double deltaFrame = 0.0;
    private long updates = 0L;
    private long frames = 0L;
    private Chronometer chronometer;

    public LanceOfDestiny(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        levelHandler = levelPanel.getLevelHandler();
        levelPanel.requestFocusInWindow();
        currentGameState = GameState.MENU;
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
        while (true) {
            changeState();
            if (state != null) {
                state.update();
            } else {  // TODO: Change this else statement whenever implemented other game states.
                try {
                    Thread.sleep(1000 / UPS_SET);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void changeState(){
        switch(currentGameState){
            case PLAYING -> state = new PlayingState(this);
            case PAUSE -> state = new PauseSPState(this);
            default -> state = null;
        }
    }

    private void startGameLoop() {
        Thread gameThread = new Thread(this);
        gameThread.start();
    }

    public double getTimePerFrame(){
        return 1_000_000_000.0 / FPS_SET;
    }

    public double getTimePerUpdate(){
        return 1_000_000_000.0 / UPS_SET;
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

    public double getDeltaUpdate() {
        return deltaUpdate;
    }

    public void setDeltaUpdate(double deltaUpdate) {
        this.deltaUpdate = deltaUpdate;
    }

    public double getDeltaFrame() {
        return deltaFrame;
    }

    public void setDeltaFrame(double deltaFrame) {
        this.deltaFrame = deltaFrame;
    }

    public long getUpdates() {
        return updates;
    }

    public void setUpdates(long updates) {
        this.updates = updates;
    }

    public long getFrames() {
        return frames;
    }

    public void setFrames(long frames) {
        this.frames = frames;
    }
}

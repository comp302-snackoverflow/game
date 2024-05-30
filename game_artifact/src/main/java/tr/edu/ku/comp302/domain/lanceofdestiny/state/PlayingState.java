package tr.edu.ku.comp302.domain.lanceofdestiny.state;
import tr.edu.ku.comp302.chrono.Chronometer;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.panel.LevelPanel;

import java.awt.*;

public class PlayingState implements State{

    private LanceOfDestiny lanceOfDestiny;


    public PlayingState(LanceOfDestiny lanceOfDestiny){
        this.lanceOfDestiny = lanceOfDestiny;
    }
    @Override
    public void update() {
        LevelHandler levelHandler = lanceOfDestiny.getLevelHandler();
        if (levelHandler.getLevel() != null){
            int upsSet = lanceOfDestiny.getUPS_SET();
            Chronometer chronometer = lanceOfDestiny.getChronometer();
            long currentTime = chronometer.getCurrentTime();

            if (chronometer.getPreviousTime() == 0) {
                chronometer.setPreviousTime(currentTime);
            }
            if (chronometer.getPauseStartTime() != null){
                chronometer.addPauseTime(chronometer.getCurrentTime() - chronometer.getPauseStartTime());
            }
            double incU = (currentTime - chronometer.getPreviousTime()) / lanceOfDestiny.getTimePerUpdate();
            double incF = (currentTime - chronometer.getPreviousTime()) / lanceOfDestiny.getTimePerFrame();
            lanceOfDestiny.setDeltaUpdate(lanceOfDestiny.getDeltaUpdate() +  incU);
            lanceOfDestiny.setDeltaFrame(lanceOfDestiny.getDeltaFrame() + incF);

            if (lanceOfDestiny.getDeltaUpdate() >= 1) {
                levelHandler.handleGameLogic(currentTime, chronometer, upsSet);
                lanceOfDestiny.setDeltaUpdate(lanceOfDestiny.getDeltaUpdate() - 1);
                lanceOfDestiny.setUpdates(lanceOfDestiny.getUpdates() + 1);
            }
            if (lanceOfDestiny.getDeltaFrame() >= 1){
                render();
                lanceOfDestiny.setDeltaFrame(lanceOfDestiny.getDeltaFrame() - 1);
                lanceOfDestiny.setFrames(lanceOfDestiny.getFrames() + 1);
            }
            chronometer.setPreviousTime(currentTime);
        }
    }


    private void render() {
        LevelPanel levelPanel = lanceOfDestiny.getLevelPanel();
        int width = (int) levelPanel.getSize().getWidth();
        int height = (int) levelPanel.getSize().getHeight();
        if (LanceOfDestiny.getScreenWidth() != width || LanceOfDestiny.getScreenHeight() != height) {
            levelPanel.setPanelSize(new Dimension(width, height));
            LanceOfDestiny.setScreenWidth(width);
            LanceOfDestiny.setScreenHeight(height);
        }
        levelPanel.repaint();
    }
}

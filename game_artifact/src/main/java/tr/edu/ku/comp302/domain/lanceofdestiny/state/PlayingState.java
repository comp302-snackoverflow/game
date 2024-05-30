package tr.edu.ku.comp302.domain.lanceofdestiny.state;
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
    }

    @Override
    public void render() {
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

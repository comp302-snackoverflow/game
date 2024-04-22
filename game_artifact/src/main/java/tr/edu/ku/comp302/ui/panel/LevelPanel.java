package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.view.LanceView;

import javax.swing.*;
import java.awt.*;

public class LevelPanel extends JPanel {
    private Level level;
    private LanceView lanceView;


    public LevelPanel(Level level, LanceView lanceView) {
        this.level = level;
        this.lanceView = lanceView;
        addKeyListener(new KeyboardHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        lanceView.render(g);
    }
    // TODO: Handle this method later.
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        lanceView.getLance().adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        lanceView.setLanceImage();      // reset image to its original sizes
        lanceView.setLanceImage(ImageHandler.resizeImage(lanceView.getLanceImage(),
                (int) lanceView.getLance().getLength(),
                (int) lanceView.getLance().getThickness()));
        // TODO: Add other entities
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LanceView getLanceView() {
        return lanceView;
    }

    public void setLanceView(LanceView lanceView) {
        this.lanceView = lanceView;
    }
}

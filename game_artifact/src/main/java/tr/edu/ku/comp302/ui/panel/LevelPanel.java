package tr.edu.ku.comp302.ui.panel;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;


import javax.swing.*;
import java.awt.*;


public class LevelPanel extends JPanel {
    private LevelHandler levelHandler;

    public LevelPanel(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
        addKeyListener(new KeyboardHandler());
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        levelHandler.renderLance(g);
        levelHandler.renderFireBall(g);
        levelHandler.renderBarriers(g);
        levelHandler.renderRemains(g);
    }

    // TODO: Handle this method later.
    public void setPanelSize(Dimension size){
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        // TODO: Add other entities
        levelHandler.getLance().adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeLanceImage();
        levelHandler.getFireBall().updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeFireBallImage();

        /*
        barriers.forEach(barrierView -> {
            barrierView.getBarrier().setL(size.getWidth() / 10.0);
            barrierView.setBarrierImage(ImageHandler.resizeImage(
                    barrierView.getBarrierImage(),
                    (int) barrierView.getBarrier().getLength(),
                    (int) barrierView.getBarrier().getThickness()
            ));
        });
         */
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }

    public void setLevelHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
    }
}

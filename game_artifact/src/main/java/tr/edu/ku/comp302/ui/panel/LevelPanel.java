package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.GameState;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class LevelPanel extends JPanel {
    private final JButton pauseButton;
    private LevelHandler levelHandler;

    public LevelPanel(LevelHandler levelHandler, MainFrame mainFrame) {
        this.levelHandler = levelHandler;
        addKeyListener(new KeyboardHandler());
        setFocusable(true);
        pauseButton = new JButton("PAUSE");
        pauseButton.addActionListener(e -> {
            LanceOfDestiny.setCurrentGameState(GameState.PAUSE_MENU);
            mainFrame.showPausePanel();
        });
        pauseButton.setBounds(0, 0, 10, 20);
        this.add(pauseButton);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        levelHandler.renderLance(g);
        levelHandler.renderFireBall(g);
        levelHandler.renderBarriers(g);
        levelHandler.renderRemains(g);
    }

    public void setPanelSize(Dimension size) {
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        levelHandler.getLance().adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeLanceImage();
        levelHandler.getFireBall().updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeFireBallImage();

        levelHandler.resizeBarrierImages();
        levelHandler.getBarriers().forEach(barrier -> {
            barrier.adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight());
        });

        levelHandler.resizeRemainImage();
        levelHandler.getRemains().forEach(remain -> remain.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight()));
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }

    public void setLevelHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
    }
}

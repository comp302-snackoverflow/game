package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.ui.panel.LevelPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private LevelPanel levelPanel;

    public MainFrame(LevelPanel levelPanel) {
        this.levelPanel = levelPanel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(levelPanel);
        setLocationRelativeTo(null);
        // setResizable(false);
        levelPanel.setPanelSize(new Dimension(1280, 800));
        pack();
        levelPanel.repaint();
        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();
        setVisible(true);
    }


}

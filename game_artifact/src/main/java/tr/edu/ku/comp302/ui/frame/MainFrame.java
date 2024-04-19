package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.ui.panel.LevelPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel panel;

    public MainFrame(JPanel panel) {
        this.panel = panel;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setLocationRelativeTo(null);
        setResizable(false);
        Dimension size = new Dimension(1280, 720);
        panel.setMinimumSize(size);
        panel.setPreferredSize(size);
        panel.setMaximumSize(size);
        
        pack();
        panel.repaint();
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        setVisible(true);

        
    }
}

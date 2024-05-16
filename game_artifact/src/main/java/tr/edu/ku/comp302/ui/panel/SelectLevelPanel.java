package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class SelectLevelPanel extends JPanel {
    private final MainFrame mainFrame;

    public SelectLevelPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

    }
}

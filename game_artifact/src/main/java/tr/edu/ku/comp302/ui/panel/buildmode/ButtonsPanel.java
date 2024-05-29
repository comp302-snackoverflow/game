package tr.edu.ku.comp302.ui.panel.buildmode;

import javax.swing.*;
import java.awt.*;

public class ButtonsPanel extends JPanel {
    protected final JButton saveButton;
    protected final JButton playButton;
    protected final JButton clearButton;
    protected final JButton exitButton;

    public ButtonsPanel() {
        GridLayout layout = new GridLayout(2, 2);
        layout.setHgap(2);
        layout.setVgap(2);
        setLayout(layout);

        saveButton = new JButton("Save");
        playButton = new JButton("Play");
        clearButton = new JButton("Reset");
        exitButton = new JButton("Exit");

        add(saveButton);
        add(playButton);
        add(clearButton);
        add(exitButton);
    }
}

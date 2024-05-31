package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.event.KeyPressHandler;
import tr.edu.ku.comp302.domain.listeners.SaveListener;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PauseMenuPanel extends JPanel {
    private final JButton resumeGameButton;
    private final JButton optionsButton;
    private final JButton saveButton;
    private final JButton mainMenuButton;
    private final MainFrame mainFrame;
    private SaveListener saveListener;
    private BufferedImage backgroundImage;

    public PauseMenuPanel(MainFrame mainFrame) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gridBagLayout);
        this.mainFrame = mainFrame;

        resumeGameButton = new JButton("Resume Game");
        optionsButton = new JButton("Options");
        saveButton = new JButton("Save");
        mainMenuButton = new JButton("Return to Main Menu");

        resumeGameButton.addActionListener(e -> mainFrame.showLevelPanel());
        optionsButton.addActionListener(e -> {
            // mainFrame.showOptionsPanel();
        });
        optionsButton.addActionListener(e -> {
            // FIXME: @ayazici21
        });
        saveButton.addActionListener(e -> this.handleSave());

        mainMenuButton.addActionListener(e -> {
            mainFrame.setCurrentLevel(null);
            mainFrame.showMainMenuPanel();
        });

        KeyPressHandler.bindKeyPressAction(this, "ESCAPE", e -> resumeGameButton.doClick());

        Dimension buttonSize = new Dimension(150, 30);
        resumeGameButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        saveButton.setPreferredSize(buttonSize);
        mainMenuButton.setPreferredSize(buttonSize);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        this.add(resumeGameButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        this.add(optionsButton, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        this.add(Box.createHorizontalStrut(50), gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        this.add(saveButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        this.add(mainMenuButton, gbc);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/light_sat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSaveListener(SaveListener saveListener) {
        this.saveListener = saveListener;
    }

    public void handleSave() {
        if (saveListener == null) {
            throw new IllegalStateException("This should not have happened. Debug me!");
        }
        if (saveListener.save()) {
            JOptionPane.showMessageDialog(this, "Game saved successfully!", "Save", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Game could not be saved!", "Save", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
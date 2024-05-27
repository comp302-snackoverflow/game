package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.lanceofdestiny.GameState;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.services.SessionManager;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    private final JButton newGameButton;
    private final JButton multiplayerButton;
    private final JButton loadGameButton;
    private final JButton createCustomMapButton;
    private final JButton helpButton;
    private final JButton optionsButton;
    private final JButton logOutButton;

    public MainMenuPanel(MainFrame mainFrame) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gridBagLayout);

        newGameButton = new JButton("New Game");
        multiplayerButton = new JButton("Multiplayer");
        loadGameButton = new JButton("Load Game");
        createCustomMapButton = new JButton("Create Custom Map");
        helpButton = new JButton("Help");
        optionsButton = new JButton("Options");
        logOutButton = new JButton("Log Out");

        newGameButton.addActionListener(e -> {
            mainFrame.showSelectLevelPanel();
            LanceOfDestiny.setCurrentGameState(GameState.NEW_GAME);
        });

        multiplayerButton.addActionListener(e -> {
            mainFrame.showMultiplayerPanel();
        });

        loadGameButton.addActionListener(e -> {
            // TODO: Implement load game
        });

        createCustomMapButton.addActionListener(e -> {
            mainFrame.showBuildPanel();
        });

        helpButton.addActionListener(e -> {
//            mainFrame.showHelpPanel();
        });

        optionsButton.addActionListener(e -> {
//            mainFrame.showOptionsPanel();
        });

        logOutButton.addActionListener(e -> {
            SessionManager.getSession().clear();
            mainFrame.showLoginPanel();
        });

        Dimension buttonSize = new Dimension(150, 30);
        newGameButton.setPreferredSize(buttonSize);
        loadGameButton.setPreferredSize(buttonSize);
        createCustomMapButton.setPreferredSize(buttonSize);
        helpButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        logOutButton.setPreferredSize(buttonSize);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        this.add(newGameButton, gbc);

        gbc.gridy++;
        this.add(multiplayerButton, gbc);

        gbc.gridy++;
        this.add(loadGameButton, gbc);

        gbc.gridy++;
        this.add(createCustomMapButton, gbc);

        gbc.gridy++;
        this.add(helpButton, gbc);

        gbc.gridy++;
        this.add(optionsButton, gbc);

        gbc.gridy++;
        this.add(logOutButton, gbc);
    }
}

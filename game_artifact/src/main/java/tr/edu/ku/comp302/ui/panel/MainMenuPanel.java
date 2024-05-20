package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.lanceofdestiny.GameState;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.services.SessionManager;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class MainMenuPanel extends JPanel {
    protected JButton newGameButton;
    protected JButton loadGameButton;
    protected JButton createCustomMapButton;
    protected JButton helpButton;
    protected JButton optionsButton;
    protected JButton logOutButton;

    public MainMenuPanel(MainFrame mainFrame) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gridBagLayout);

        newGameButton = new JButton("New Game");
        loadGameButton = new JButton("Load Game");
        createCustomMapButton = new JButton("Create Custom Map");
        helpButton = new JButton("Help");
        optionsButton = new JButton("Options");
        logOutButton = new JButton("Log Out");

        newGameButton.addActionListener(e -> {
            mainFrame.showSelectLevelPanel();
            LanceOfDestiny.setCurrentGameState(GameState.NEW_GAME);
        });
        loadGameButton.addActionListener(e -> {
            // Not implemented
            assert false; // FIXME: implement LoadGamePanel

        });
        createCustomMapButton.addActionListener(e -> {
            mainFrame.showBuildPanel();
        });
        helpButton.addActionListener(e -> {
//            mainFrame.showHelpPanel();
        });
        optionsButton.addActionListener(e -> {
            System.out.println("Options");
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

        gbc.gridy = 1;
        this.add(loadGameButton, gbc);

        gbc.gridy = 2;
        this.add(createCustomMapButton, gbc);

        gbc.gridy = 3;
        this.add(helpButton, gbc);

        gbc.gridy = 4;
        this.add(optionsButton, gbc);

        gbc.gridy = 5;
        this.add(logOutButton, gbc);
    }
}

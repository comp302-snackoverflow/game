package tr.edu.ku.comp302.ui.panel;

import javax.swing.*;
import java.awt.*;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.GameState;
import tr.edu.ku.comp302.domain.services.save.LoadService;
import tr.edu.ku.comp302.domain.services.save.SaveService;
import tr.edu.ku.comp302.ui.frame.MainFrame;

public class MainMenuPanel extends JPanel {
    protected JButton newGameButton;
    protected JButton loadGameButton;
    protected JButton createCustomMapButton;
    protected JButton helpButton;
    protected JButton optionsButton;
    protected JButton quitButton;

    public MainMenuPanel(MainFrame mainFrame) {
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        setLayout(gridBagLayout);

        newGameButton = new JButton("New Game");
        loadGameButton = new JButton("Load Game");
        createCustomMapButton = new JButton("Create Custom Map");
        helpButton = new JButton("Help");
        optionsButton = new JButton("Options");
        quitButton = new JButton("Quit");

        newGameButton.addActionListener(e -> {
            mainFrame.showLevelPanel();
            LanceOfDestiny.setCurrentGameState(GameState.NEW_GAME);
        });
        loadGameButton.addActionListener(e -> {

            LevelPanel lp = LoadService.getInstance().loadMap(9, mainFrame);
            mainFrame.setLevelPanel(lp);
            mainFrame.showLevelPanel();
            LanceOfDestiny.setCurrentGameState(GameState.LOAD_GAME);
        });
        createCustomMapButton.addActionListener(e -> {
            mainFrame.showBuildPanel();
            LanceOfDestiny.setCurrentGameState(GameState.CREATE_CUSTOM_MAP);
        });
        helpButton.addActionListener(e -> {
            LanceOfDestiny.setCurrentGameState(GameState.HELP);

        });
        optionsButton.addActionListener(e -> {
            LanceOfDestiny.setCurrentGameState(GameState.OPTIONS);
            System.out.println("Options");
        });
        quitButton.addActionListener(e -> {
            System.exit(0);
            // TODO: Change this later.
        });

        Dimension buttonSize = new Dimension(150, 30);
        newGameButton.setPreferredSize(buttonSize);
        loadGameButton.setPreferredSize(buttonSize);
        createCustomMapButton.setPreferredSize(buttonSize);
        helpButton.setPreferredSize(buttonSize);
        optionsButton.setPreferredSize(buttonSize);
        quitButton.setPreferredSize(buttonSize);

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
        this.add(quitButton, gbc);
    }
}

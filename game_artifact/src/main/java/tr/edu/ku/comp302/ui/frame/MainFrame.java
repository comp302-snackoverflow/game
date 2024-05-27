package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.GameState;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.panel.*;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String LOGIN = "login";
    private static final String REGISTER = "register";
    private static final String MAINMENU = "mainmenu";
    private static final String LEVEL = "level";
    private static final String BUILD = "build";
    private static final String PAUSE = "pause";
    private static final String SELECT_LEVEL = "select_level";
    private static final String MULTIPLAYER = "multiplayer";
    private static final String MULTIPLAYER_SELECT_LEVEL = "multiplayer_select_level";
    private static final String CREATE_GAME_PANEL = "create_game_panel";
    private static final String JOIN_GAME_PANEL = "join_game_panel";
    private static final int frameWidth = 1294;
    private static final int frameHeight = 837;
    private final JPanel cards;
    private final CardLayout layout;
    private LoginPanel loginPanel;
    private JPanel registerPanel;
    private JPanel mainMenuPanel;
    private LevelPanel levelPanel;
    private JPanel buildPanel;
    private JPanel pausePanel;
    private SelectLevelPanel selectLevelPanel;
    private JPanel multiplayerPanel;
    private SelectLevelPanel multiplayerSelectLevelPanel;
    private CreateGamePanel createGamePanel;
    private JPanel joinGamePanel;

    private MainFrame() {
        setTitle("Lance of Destiny"); // TODO: Maybe change the title in every page

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        cards = new JPanel(layout);
        add(cards);

    }

    public static MainFrame createMainFrame() {
        MainFrame self = new MainFrame();
        self.prepareLoginPanel();
        self.prepareRegisterPanel();
        self.prepareMainMenu();
        self.prepareBuildPanel();
        self.prepareLevelPanel();
        self.preparePausePanel();
        self.prepareSelectLevelPanel();
        self.prepareMultiplayerPanel();
        self.prepareMultiplayerSelectLevelPanel();
        self.prepareCreateGamePanel();
        self.prepareJoinGamePanel();
        self.cards.add(self.loginPanel, LOGIN);
        self.cards.add(self.registerPanel, REGISTER);
        self.cards.add(self.mainMenuPanel, MAINMENU);
        self.cards.add(self.levelPanel, LEVEL);
        self.cards.add(self.buildPanel, BUILD);
        self.cards.add(self.pausePanel, PAUSE);
        self.cards.add(self.selectLevelPanel, SELECT_LEVEL);
        self.cards.add(self.multiplayerPanel, MULTIPLAYER);
        self.cards.add(self.multiplayerSelectLevelPanel, MULTIPLAYER_SELECT_LEVEL);
        self.cards.add(self.createGamePanel, CREATE_GAME_PANEL);
        self.cards.add(self.joinGamePanel, JOIN_GAME_PANEL);
        return self;
    }

    private void prepareLoginPanel() {
        loginPanel = new LoginPanel(this);
        loginPanel.setBounds(0, 11, 804, 781);
    }

    private void prepareRegisterPanel() {
        registerPanel = new RegisterPanel(this);
        registerPanel.setBounds(0, 11, 804, 781);
    }

    private void prepareMainMenu() {
        mainMenuPanel = new MainMenuPanel(this);
        mainMenuPanel.setBounds(0, 11, 804, 781);
    }

    private void prepareLevelPanel() {
        levelPanel = new LevelPanel(new LevelHandler(null), this);
        levelPanel.repaint();
        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();
    }

    private void prepareBuildPanel() {
        buildPanel = BuildPanel.createPanel(this);
        buildPanel.repaint();
        buildPanel.requestFocusInWindow();
    }

    private void preparePausePanel() {
        pausePanel = new PauseMenuPanel(this);
        pausePanel.setBounds(0, 11, 804, 781);
        pausePanel.requestFocusInWindow();
    }

    private void prepareSelectLevelPanel() {
        selectLevelPanel = new SelectLevelPanel(this, false);
    }

    private void prepareMultiplayerPanel() {
        multiplayerPanel = new MultiplayerPanel(this);
    }

    private void prepareMultiplayerSelectLevelPanel() {
        multiplayerSelectLevelPanel = new SelectLevelPanel(this, true);
    }

    private void prepareCreateGamePanel() {
        createGamePanel = new CreateGamePanel(this);
    }

    private void prepareJoinGamePanel() {
        joinGamePanel = new JoinGamePanel(this);
    }

    public void showLoginPanel() {
        layout.show(cards, LOGIN);
        LanceOfDestiny.setCurrentGameState(GameState.LOGIN_MENU);
        refresh();
    }

    public void showRegisterPanel() {
        layout.show(cards, REGISTER);
        LanceOfDestiny.setCurrentGameState(GameState.REGISTER_MENU);
        refresh();
    }

    public void showMainMenuPanel() {
        layout.show(cards, MAINMENU);
        LanceOfDestiny.setCurrentGameState(GameState.MAIN_MENU);
        refresh();
    }

    public void showLevelPanel() {
        layout.show(cards, LEVEL);
        levelPanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.PLAYING);
        refresh();
    }

    public void showBuildPanel() {
        layout.show(cards, BUILD);
        LanceOfDestiny.setCurrentGameState(GameState.CREATE_CUSTOM_MAP);
        refresh();
    }

    public void showPausePanel() {
        layout.show(cards, PAUSE);
        LanceOfDestiny.setCurrentGameState(GameState.PAUSE_MENU);
        refresh();
    }

    public void showSelectLevelPanel() {
        layout.show(cards, SELECT_LEVEL);
        selectLevelPanel.updateLevels();
        LanceOfDestiny.setCurrentGameState(GameState.NEW_GAME);
        refresh();
    }

    public void showMultiplayerPanel() {
        layout.show(cards, MULTIPLAYER);
        LanceOfDestiny.setCurrentGameState(GameState.MULTIPLAYER);
        refresh();
    }

    public void showMultiplayerSelectLevelPanel() {
        layout.show(cards, MULTIPLAYER_SELECT_LEVEL);
        multiplayerSelectLevelPanel.updateLevels();
        LanceOfDestiny.setCurrentGameState(GameState.MULTIPLAYER_NEW_GAME);
        refresh();
    }

    public void showCreateGamePanel() {
        layout.show(cards, CREATE_GAME_PANEL);
        LanceOfDestiny.setCurrentGameState(GameState.MULTIPLAYER_CREATE_GAME);
        refresh();
    }

    public void showJoinGamePanel() {
        layout.show(cards, JOIN_GAME_PANEL);
        LanceOfDestiny.setCurrentGameState(GameState.MULTIPLAYER_JOIN_GAME);
        refresh();
    }

    public void setMultiplayerLevel(int levelId) {
        createGamePanel.updateGameCode(levelId);
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    public JPanel getBuildPanel() {
        return buildPanel;
    }

    // FIXME: bad practice. Find a way to set the level without passing it to the UI
    public void setCurrentLevel(Level level) {
        levelPanel.getLevelHandler().setLevel(level);
    }

}

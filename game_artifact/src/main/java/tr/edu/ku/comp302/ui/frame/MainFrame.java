package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.lanceofdestiny.state.GameState;
import tr.edu.ku.comp302.ui.panel.*;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String LOGIN = "login";
    private static final String GAMEOVER = "login";
    private static final String REGISTER = "register";
    private static final String MAINMENU = "mainmenu";
    private static final String LEVEL = "level";
    private static final String BUILD = "build";
    private static final String PAUSE = "pause";
    private static final String SELECT_LEVEL = "select_level";
    private static final String SELECT_SAVE = "select_save";
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
    private PauseMenuPanel pausePanel;
    private SelectLevelPanel selectLevelPanel;
    private SelectLoadPanel selectLoadPanel;
    private JPanel multiplayerPanel;
    private SelectLevelPanel multiplayerSelectLevelPanel;
    private CreateGamePanel createGamePanel;
    private JPanel joinGamePanel;
    private LanceOfDestiny lod;
    private GameOverPanel gameOverPanel;

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
        self.prepareSelectSavedGamePanel();
        self.lod.addListener(self.levelPanel);
        self.lod.addListener(self.levelPanel.getLevelHandler().getSpellHandler());
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
        self.cards.add(self.selectLoadPanel, SELECT_SAVE);

        self.setMinimumSize(new Dimension(frameWidth, frameHeight));
        JFrame.setDefaultLookAndFeelDecorated(true);
        return self;
    }

    private void prepareGameOverPanel(boolean isWon, int score) {
        gameOverPanel = new GameOverPanel(isWon, score,this);
        gameOverPanel.setFocusable(true);
    }

    private void prepareLoginPanel() {
        loginPanel = new LoginPanel(this);
        loginPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareRegisterPanel() {
        registerPanel = new RegisterPanel(this);
        registerPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareMainMenu() {
        mainMenuPanel = new MainMenuPanel(this);
        mainMenuPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareLevelPanel() {
        LevelHandler levelHandler = new LevelHandler(null);
        levelPanel = new LevelPanel(levelHandler, this);
        levelHandler.setLevelPanel(levelPanel);
        lod = new LanceOfDestiny(levelPanel);
        levelPanel.setPauseListener(lod);
        levelPanel.repaint();
        levelPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareBuildPanel() {
        buildPanel = BuildPanel.createPanel(this);
        buildPanel.repaint();
        buildPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void preparePausePanel() {
        pausePanel = new PauseMenuPanel(this);
        pausePanel.setResumeListener(lod);
        pausePanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareSelectLevelPanel() {
        selectLevelPanel = new SelectLevelPanel(this, false);
        selectLevelPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareMultiplayerPanel() {
        multiplayerPanel = new MultiplayerPanel(this);
        multiplayerPanel.setFocusable(true);
    }

    private void prepareSelectSavedGamePanel() {
        selectLoadPanel = new SelectLoadPanel(this);
        selectLoadPanel.setFocusable(true);
        revalidate();
        repaint();
    }

    private void prepareMultiplayerSelectLevelPanel() {
        multiplayerSelectLevelPanel = new SelectLevelPanel(this, true);
        multiplayerSelectLevelPanel.setFocusable(true);
    }

    private void prepareCreateGamePanel() {
        createGamePanel = new CreateGamePanel(this);
        createGamePanel.setFocusable(true);
    }

    private void prepareJoinGamePanel() {
        joinGamePanel = new JoinGamePanel(this);
        joinGamePanel.setFocusable(true);
        revalidate();
        repaint();
    }

    public void showGameOverPanel(boolean isWon, int score) {
        prepareGameOverPanel(isWon, score);
        cards.add(gameOverPanel, GAMEOVER);
        layout.show(cards, GAMEOVER);
        gameOverPanel.requestFocusInWindow();
        refresh();
    }

    public void showLoginPanel() {
        layout.show(cards, LOGIN);
        loginPanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        refresh();
    }

    public void showRegisterPanel() {
        layout.show(cards, REGISTER);
        registerPanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        refresh();
    }

    public void showMainMenuPanel() {
        layout.show(cards, MAINMENU);
        mainMenuPanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        refresh();
    }

    public void showLevelPanel(P2PConnection conn) {
        layout.show(cards, LEVEL);
        levelPanel.requestFocusInWindow();
        levelPanel.setPanelSize(new Dimension(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight()));
        lod.setConnection(conn);
        if (conn != null) {
            conn.setMessageListener(lod);
            conn.setMessageSender(lod);
            LanceOfDestiny.setCurrentGameState(GameState.MP_PLAYING);
        } else {
            LanceOfDestiny.setCurrentGameState(GameState.PLAYING);
        }
        refresh();
    }
    public void showLevelPanel() {
        layout.show(cards, LEVEL);
        levelPanel.requestFocusInWindow();
        levelPanel.setPanelSize(new Dimension(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight()));
        LanceOfDestiny.setCurrentGameState(GameState.PLAYING);
        refresh();
    }

    public void showBuildPanel() {
        layout.show(cards, BUILD);
        buildPanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        refresh();
    }

    public void showPausePanel(boolean isMultiplayer) {
        layout.show(cards, PAUSE);
        pausePanel.requestFocusInWindow();
        if (isMultiplayer) {
            LanceOfDestiny.setCurrentGameState(GameState.MP_PAUSE);
        } else {
            LanceOfDestiny.setCurrentGameState(GameState.PAUSE);
        }
        refresh();
    }

    public void showPausePanel() {
        layout.show(cards, PAUSE);
        pausePanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.PAUSE);
        refresh();
    }

    public void showSelectLevelPanel() {
        layout.show(cards, SELECT_LEVEL);
        selectLevelPanel.requestFocusInWindow();
        selectLevelPanel.updateLevels();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        refresh();
    }

    public void showSelectSavedGamePanel() {
        layout.show(cards, SELECT_SAVE);
        selectLoadPanel.updateLevels();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        selectLoadPanel.requestFocusInWindow();
        refresh();
    }

    public void showMultiplayerPanel() {
        layout.show(cards, MULTIPLAYER);
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        multiplayerPanel.requestFocusInWindow();
        refresh();
    }

    public void showMultiplayerSelectLevelPanel() {
        layout.show(cards, MULTIPLAYER_SELECT_LEVEL);
        multiplayerSelectLevelPanel.updateLevels();
        multiplayerSelectLevelPanel.requestFocusInWindow();
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        refresh();
    }

    public void showCreateGamePanel() {
        layout.show(cards, CREATE_GAME_PANEL);
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        createGamePanel.requestFocusInWindow();
        refresh();
    }

    public void showJoinGamePanel() {
        layout.show(cards, JOIN_GAME_PANEL);
        LanceOfDestiny.setCurrentGameState(GameState.MENU);
        joinGamePanel.requestFocusInWindow();
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
        if (level == null) {
            lod.setConnection(null);
        }
        pausePanel.setSaveListener(level);
        levelPanel.getLevelHandler().setLevel(level);
    }
}

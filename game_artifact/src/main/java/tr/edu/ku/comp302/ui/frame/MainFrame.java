package tr.edu.ku.comp302.ui.frame;

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
        self.prepareSelectSavedGamePanel();

        self.cards.add(self.loginPanel, LOGIN);
        self.cards.add(self.registerPanel, REGISTER);
        self.cards.add(self.mainMenuPanel, MAINMENU);
        self.cards.add(self.levelPanel, LEVEL);
        self.cards.add(self.buildPanel, BUILD);
        self.cards.add(self.pausePanel, PAUSE);
        self.cards.add(self.selectLevelPanel, SELECT_LEVEL);
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
    }

    private void prepareRegisterPanel() {
        registerPanel = new RegisterPanel(this);
        registerPanel.setFocusable(true);
    }

    private void prepareMainMenu() {
        mainMenuPanel = new MainMenuPanel(this);
        mainMenuPanel.setFocusable(true);
    }

    private void prepareLevelPanel() {
        LevelHandler levelHandler = new LevelHandler(null);
        levelPanel = new LevelPanel(levelHandler, this);
        levelHandler.setLevelPanel(levelPanel);
        new LanceOfDestiny(levelPanel);
        levelPanel.repaint();
        levelPanel.setFocusable(true);
    }

    private void prepareBuildPanel() {
        buildPanel = BuildPanel.createPanel(this);
        buildPanel.repaint();
        buildPanel.setFocusable(true);
    }

    private void preparePausePanel() {
        pausePanel = new PauseMenuPanel(this);
        pausePanel.setFocusable(true);
    }

    private void prepareSelectLevelPanel() {
        selectLevelPanel = new SelectLevelPanel(this);
        selectLevelPanel.setFocusable(true);
    }

    private void prepareSelectSavedGamePanel() {
        selectLoadPanel = new SelectLoadPanel(this);
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
        refresh();
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
        pausePanel.setSaveListener(level);
        levelPanel.getLevelHandler().setLevel(level);
    }
}

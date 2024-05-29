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
        self.cards.add(self.loginPanel, LOGIN);
        self.cards.add(self.registerPanel, REGISTER);
        self.cards.add(self.mainMenuPanel, MAINMENU);
        self.cards.add(self.levelPanel, LEVEL);
        self.cards.add(self.buildPanel, BUILD);
        self.cards.add(self.pausePanel, PAUSE);
        self.cards.add(self.selectLevelPanel, SELECT_LEVEL);
        self.setMinimumSize(new Dimension(frameWidth, frameHeight));
        JFrame.setDefaultLookAndFeelDecorated(true);
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
        new LanceOfDestiny(levelPanel);
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
        selectLevelPanel = new SelectLevelPanel(this);

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

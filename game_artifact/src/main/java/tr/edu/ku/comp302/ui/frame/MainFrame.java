package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.App;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.panel.*;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;
import tr.edu.ku.comp302.ui.view.View;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class MainFrame extends JFrame {
    private static final String LOGIN = "login";
    private static final String REGISTER = "register";
    private static final String MAINMENU = "mainmenu";
    private static final String LEVEL = "level";
    private static final String BUILD = "build";

    private final JPanel cards;
    private LoginPanel loginPanel;
    private JPanel registerPanel;
    private JPanel mainMenuPanel;
    private JPanel levelPanel;
    private JPanel buildPanel;
    private final CardLayout layout;

    private static final int frameWidth = 1280;
    private static final int frameHeight = 720;

    private MainFrame() {
        setTitle("Lance of Destiny"); // TODO: Maybe change the title in every page

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // setResizable(false);
        pack();
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null);

        layout = new CardLayout();
        cards = new JPanel(layout);
        add(cards);

    }

    private void prepareLoginPanel() {
        loginPanel = new LoginPanel(this);
        loginPanel.setBounds(0, 11, 804, 781);
        loginPanel.setLayout(null);
    }

    private void prepareRegisterPanel() {
        registerPanel = new RegisterPanel(this);
        registerPanel.setBounds(0, 11, 804, 781);
        registerPanel.setLayout(null);
    }

    private void prepareMainMenu() {
        mainMenuPanel = new MainMenuPanel(this);
        mainMenuPanel.setBounds(0, 11, 804, 781);
    }



    /**
     * Prepares the level panel by initializing the game elements and setting up the level.
     * 
     * @see Lance
     * @see FireBall
     * @see App
     * @see Level
     * @see LevelPanel
     * @see LevelHandler
     */
    private void prepareLevelPanel() {
        // Initialize Lance and FireBall objects
        Lance lance = new Lance(576, 600);
        FireBall fb = new FireBall(600, 560);

        // Generate barriers using the App class
        var barriers = App.generateBarriers(LanceOfDestiny.getScreenWidth(),
                LanceOfDestiny.getScreenHeight(), new BuildPanelModel());

        // Create a new level with the initialized objects
        Level level = new Level(lance, fb, barriers);

        // Create a new LevelPanel with the LevelHandler
        levelPanel = new LevelPanel(new LevelHandler(level));

        // TODO: Investigate and fix the following lines
        // levelPanel = new LevelPanel(level, lv); // FIX THIS
        // levelPanel = new LevelPanel(level, lv,
        // new FireBallView(new FireBall(600, 560, frameWidth, frameHeight))); // FIX THIS AS WELL

        // Set panel size and request focus for interaction
        // (LevelPanel) levelPanel).setPanelSize(new Dimension(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight()))
        levelPanel.repaint();
        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();
    }

    private void prepareBuildPanel() {
        buildPanel = new BuildPanel(this);
        buildPanel.repaint();
        buildPanel.setLayout(null);
        buildPanel.requestFocusInWindow();
    }

    public static MainFrame createMainFrame() {
        MainFrame self = new MainFrame();
        self.prepareLoginPanel();
        self.prepareRegisterPanel();
        self.prepareMainMenu();
        self.prepareLevelPanel();
        self.prepareBuildPanel();
        self.cards.add(self.loginPanel, LOGIN);
        self.cards.add(self.registerPanel, REGISTER);
        self.cards.add(self.mainMenuPanel, MAINMENU);
        self.cards.add(self.levelPanel, LEVEL);
        self.cards.add(self.buildPanel, BUILD);
        return self;
    }

    public void showLoginPanel() {
        layout.show(cards, LOGIN);
        refresh();
    }

    public void showRegisterPanel() {
        layout.show(cards, REGISTER);
        refresh();
    }

    public void showMainMenuPanel() {
        layout.show(cards, MAINMENU);
        refresh();
    }

    public void showLevelPanel() {
        // TODO Level should be prepared here

        layout.show(cards, LEVEL);
        refresh();
        new LanceOfDestiny((LevelPanel) levelPanel);
    }

    public void showBuildPanel() {
        layout.show(cards, BUILD);
        refresh();
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public JPanel getBuildPanel() {
        return buildPanel;
    }
}


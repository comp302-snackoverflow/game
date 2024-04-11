package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.ui.panel.LevelPanel;
import tr.edu.ku.comp302.ui.panel.LoginPanel;
import tr.edu.ku.comp302.ui.panel.MainMenuPanel;
import tr.edu.ku.comp302.ui.panel.RegisterPanel;
import tr.edu.ku.comp302.ui.view.LanceView;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static final String LOGIN = "login";
    private static final String REGISTER = "register";
    private static final String MAINMENU = "mainmenu";
    private static final String LEVEL = "level";

    private JPanel cards;
    private LoginPanel loginPanel;
    private JPanel registerPanel;
    private JPanel mainMenuPanel;
    private JPanel levelPanel;
    private final CardLayout layout;

    private static final int frameWidth = 1280;
    private static final int frameHeight = 720;

    private MainFrame() {
        setTitle("Lance of Destiny"); // TODO: Maybe change the title in every page

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        pack();
        setSize(frameWidth, frameHeight);

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
        mainMenuPanel.setBounds(0, 11, 804, 781); // I don't know what these numbers do
        mainMenuPanel.setLayout(null);            // Just copied from login and register frames
    }

    private void prepareLevelPanel() {
        Lance lance = new Lance(576, 600);
        LanceView lv = new LanceView(lance);
        Level level = new Level();
        levelPanel = new LevelPanel(level, lv);
        ((LevelPanel) levelPanel).setPanelSize(new Dimension(1280, 800));
        levelPanel.repaint();
        levelPanel.setFocusable(true);
        levelPanel.requestFocusInWindow();
    }

    public static MainFrame createMainFrame() {
        MainFrame self = new MainFrame();
        self.prepareLoginPanel();
        self.prepareRegisterPanel();
        self.prepareMainMenu();
        self.prepareLevelPanel();
        self.cards.add(self.loginPanel, LOGIN);
        self.cards.add(self.registerPanel, REGISTER);
        self.cards.add(self.mainMenuPanel, MAINMENU);
        self.cards.add(self.levelPanel, LEVEL);
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

    private void refresh() {
        revalidate();
        repaint();
    }
}

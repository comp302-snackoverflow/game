package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;

public class MainMenuPanel extends JPanel {
    private final MainFrame mainFrame;

    private final JButton singlePlayerButton;
//    private JButton multiplayerButton; // For phase II
    private final JButton logoutButton;

    public MainMenuPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);

        // This is just for a quick demo. This panel should also have:
        // Game name
        // Single player button (when clicked should open a level selection page)
        // Multi player button (not yet)
        // Options button (If we want to limit screen resizability)
        // Credits -- why not
        // Log out button
        // Quit button
        // (Start playing Fireball here?)
        singlePlayerButton = new JButton("Start");
        singlePlayerButton.setBounds(200, 200, 100, 40);

        logoutButton = new JButton("Log Out");
        logoutButton.setBounds(200, 300, 100, 40);

        singlePlayerButton.addActionListener(e -> mainFrame.showLevelPanel());
        logoutButton.addActionListener(e -> mainFrame.showLoginPanel());

        add(singlePlayerButton);
        add(logoutButton);
    }
}


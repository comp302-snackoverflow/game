package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class MultiplayerPanel extends JPanel {
    public MultiplayerPanel(MainFrame mainFrame) {

        JLabel titleLabel = new JLabel("Multiplayer");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        JButton createGameButton = new JButton("Host a Game");
        centerPanel.add(createGameButton, gbc);

        gbc.gridy++;
        JButton joinGameButton = new JButton("Join a Game");
        centerPanel.add(joinGameButton, gbc);

        gbc.gridy++;
        JButton backButton = new JButton("Back");
        centerPanel.add(backButton, gbc);

        add(centerPanel, BorderLayout.CENTER);

        createGameButton.addActionListener(e -> mainFrame.showMultiplayerSelectLevelPanel());
        joinGameButton.addActionListener(e -> mainFrame.showJoinGamePanel());
        backButton.addActionListener(e -> mainFrame.showMainMenuPanel());
    }
}

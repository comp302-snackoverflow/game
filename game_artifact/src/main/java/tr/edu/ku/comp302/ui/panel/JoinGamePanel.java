package tr.edu.ku.comp302.ui.panel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.client.JoinInfo;
import tr.edu.ku.comp302.domain.handler.JoinGameHandler;
import tr.edu.ku.comp302.domain.services.save.LoadService;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

public class JoinGamePanel extends JPanel {
    private static Logger logger = LogManager.getLogger(JoinGamePanel.class);
    private final MainFrame mainFrame;
    private final JoinGameHandler joinGameHandler;
    private final JLabel gameCodeLabel;
    private final JTextField codeTextField;
    private final JLabel descriptionLabel;
    private final JButton pasteButton;
    private final JButton joinButton;
    private final JButton backButton;

    public JoinGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        joinGameHandler = new JoinGameHandler();

        setLayout(new BorderLayout());

        gameCodeLabel = new JLabel("Game code:");
        gameCodeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gameCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        codeTextField = new JTextField();
        codeTextField.setFont(new Font("Arial", Font.PLAIN, 14));
        codeTextField.setHorizontalAlignment(SwingConstants.CENTER);

        descriptionLabel = new JLabel("Enter a code given by your friend to play together.");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        pasteButton = new JButton("Paste code");
        pasteButton.addActionListener(e -> {
            String paste = JoinGameHandler.getClipboardText();
            if (paste != null) {
                codeTextField.setText(paste);
            }
        });

        joinButton = new JButton("Join Game");
        joinButton.addActionListener(e -> {
            String gameCode = codeTextField.getText();
            if (gameCode.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a game code.", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                JoinInfo gameInfo = joinGameHandler.joinGame(gameCode);
                if (gameInfo == null) {
                    JOptionPane.showMessageDialog(this, "An error occurred while joining the game. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    int levelId = gameInfo.levelId();
                    mainFrame.setCurrentLevel(LoadService.getInstance().loadMap(levelId));
                    mainFrame.showLevelPanel(gameInfo.conn());
                }
            }
        });

        backButton = new JButton("Back");
        backButton.addActionListener(e -> mainFrame.showMultiplayerPanel());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        centerPanel.add(gameCodeLabel, gbc);
        gbc.gridx++;
        centerPanel.add(codeTextField, gbc);
        gbc.gridx = 0;

        gbc.gridwidth = 2;
        gbc.gridy++;
        centerPanel.add(descriptionLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        centerPanel.add(pasteButton, gbc);

        gbc.gridx++;
        centerPanel.add(joinButton, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        centerPanel.add(backButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }
}

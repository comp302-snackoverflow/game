package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.CreateGameHandler;
import tr.edu.ku.comp302.domain.listeners.PeerJoinListener;
import tr.edu.ku.comp302.server.PlayerInfo;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

public class CreateGamePanel extends JPanel implements PeerJoinListener {
    private final MainFrame mainFrame;
    private final CreateGameHandler createGameHandler;

    private String gameCode;
    private JLabel gameCodeLabel;
    private JLabel descriptionLabel;
    private JButton copyButton;
    private JButton backButton;

    public CreateGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        createGameHandler = new CreateGameHandler();

        setLayout(new BorderLayout());

        gameCodeLabel = new JLabel("");
        gameCodeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gameCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        descriptionLabel = new JLabel("Share this code with your friend to play together.");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        copyButton = new JButton("Copy Code to Clipboard");
        copyButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(gameCode);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (!createGameHandler.removeGame(gameCode)) {
                // TODO: need anything here?
            }
            mainFrame.showMultiplayerSelectLevelPanel();
        });

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
        gbc.gridy++;
        centerPanel.add(descriptionLabel, gbc);

        gbc.gridy++;
        centerPanel.add(copyButton, gbc);

        gbc.gridy++;
        centerPanel.add(backButton, gbc);

        add(centerPanel, BorderLayout.CENTER);
    }

    public void updateGameCode(int levelId) {
        createGameHandler.removeGame(gameCode);
        gameCode = createGameHandler.createGame(levelId, this);
        System.out.println(gameCode);
        gameCodeLabel.setText("Game Code: " + gameCode);
        revalidate();
    }

    public void onJoin(PlayerInfo playerInfo) {
        System.out.println("Peer joined: " + playerInfo);
    }
}

package tr.edu.ku.comp302.ui.panel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.client.P2PConnection;
import tr.edu.ku.comp302.domain.handler.CreateGameHandler;
import tr.edu.ku.comp302.domain.listeners.PeerJoinListener;
import tr.edu.ku.comp302.domain.services.save.LoadService;
import tr.edu.ku.comp302.server.PlayerInfo;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Clipboard;

public class CreateGamePanel extends JPanel implements PeerJoinListener {
    private static final Logger logger = LogManager.getLogger(CreateGamePanel.class);
    private final MainFrame mainFrame;
    private final CreateGameHandler createGameHandler;

    private String gameCode;
    private final JLabel gameCodeLabel;
    private Integer levelId;

    public CreateGamePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        createGameHandler = new CreateGameHandler();

        setLayout(new BorderLayout());

        gameCodeLabel = new JLabel("");
        gameCodeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        gameCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descriptionLabel = new JLabel("Share this code with your friend to play together.");
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        JButton copyButton = new JButton("Copy Code to Clipboard");
        copyButton.addActionListener(e -> {
            StringSelection stringSelection = new StringSelection(gameCode);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            if (!createGameHandler.removeGame(gameCode)) {
                logger.warn("Failed to remove the game. Game code: " + gameCode);
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
        this.levelId = levelId;
        createGameHandler.removeGame(gameCode);
        gameCode = createGameHandler.createGame(levelId, this);
        System.out.println(gameCode);
        gameCodeLabel.setText("Game Code: " + gameCode);
        revalidate();
    }

    @Override
    public void onJoin(P2PConnection conn) {
        mainFrame.setCurrentLevel(LoadService.getInstance().loadMap(levelId));
        mainFrame.showLevelPanel(conn);
    }
}

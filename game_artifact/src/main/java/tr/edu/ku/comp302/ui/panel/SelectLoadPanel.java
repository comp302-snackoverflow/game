package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.SelectLoadHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.Level;
import tr.edu.ku.comp302.domain.services.SessionManager;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class SelectLoadPanel extends JPanel {
    private final MainFrame mainFrame;
    private final JPanel levelsPanel;
    private final SelectLoadHandler selectLoadHandler;
    private BufferedImage backgroundImage;


    public SelectLoadPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.selectLoadHandler = SelectLoadHandler.getInstance();

        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> mainFrame.showMainMenuPanel());

        headerPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("Select Level");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        levelsPanel = new JPanel(new GridBagLayout());

        JScrollPane scrollPane = new JScrollPane(levelsPanel);

        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(null);

        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/light_sat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateLevels() {
        Integer userId = (Integer) SessionManager.getSession().getSessionData("userID");
        if (userId == null) {
            JOptionPane.showMessageDialog(this, "You must be logged in to view levels. Redirecting to the login page", "Error", JOptionPane.ERROR_MESSAGE);
            mainFrame.showLoginPanel();
            return;
        }

        levelsPanel.removeAll();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        List<Integer> levels = selectLoadHandler.getLevels(userId);
        if (levels.isEmpty()) {
            JLabel noLevelsLabel = new JLabel("No levels found");
            noLevelsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noLevelsLabel.setFont(new Font("Arial", Font.BOLD, 16));
            levelsPanel.add(noLevelsLabel);
            return;
        }
        for (int i = 0; i < levels.size(); i++) {
            JPanel selectLevelButton = selectLoadHandler.generateSelectLevelButton(i + 1, levels.get(i), this::showLevel);
            gbc.gridx = i % 4;
            gbc.gridy = i / 4;
            levelsPanel.add(selectLevelButton, gbc);
        }

        revalidate();
        repaint();
    }

    private void showLevel(Level level) {
        mainFrame.setCurrentLevel(level);
        mainFrame.showLevelPanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

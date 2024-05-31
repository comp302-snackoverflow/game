package tr.edu.ku.comp302.ui.panel;

import javax.swing.*;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import java.awt.*;

public class GameOverPanel extends JPanel {
    private boolean isWon;
    private int score;
    private Image backgroundImage;
    private Image iconImage;
    private MainFrame mainFrame;
    
    public GameOverPanel(boolean isWon, int score, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.isWon = isWon;
        this.score = score;
        this.setPreferredSize(new Dimension(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight()));
        if (isWon) {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/light.png")).getImage();
        } else {
            backgroundImage = new ImageIcon(getClass().getResource("/assets/dark.png")).getImage();
            iconImage = new ImageIcon(getClass().getResource("/assets/ymir.png")).getImage();
        }
        setLayout(null); 
        JButton backButton = new JButton("Back to the Main Menu");
        backButton.addActionListener(e -> backToMainMenu());
        add(backButton);
    }

    private void backToMainMenu() {
        mainFrame.showMainMenuPanel();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    
        int largeFontSize = getWidth() / 15; 
        int smallFontSize = getWidth() / 25; 

        g.setFont(new Font("Arial", Font.BOLD, largeFontSize));
        if (isWon) {
            g.setColor(Color.DARK_GRAY);
            String message = "CONGRATULATIONS!";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - messageWidth) / 2, getHeight() / 3 - largeFontSize);
        } else {
            g.setColor(Color.WHITE);
            String message = "GAME OVER";
            int messageWidth = g.getFontMetrics().stringWidth(message);
            g.drawString(message, (getWidth() - messageWidth) / 2, getHeight() / 3 - largeFontSize);
        }
        g.setFont(new Font("Arial", Font.BOLD, smallFontSize));
        String scoreText = "SCORE: " + score;
        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);
        g.drawString(scoreText, (getWidth() - scoreWidth) / 2, (int)(getHeight() / 3));
        
        if (!isWon && iconImage != null) {
            g.drawImage(iconImage, getWidth() / 2 - 120, (int) (getHeight() / 2), 240, 240, this);
        }
        
        Component[] components = getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                int buttonWidth = getWidth() / 4;
                int buttonHeight = getHeight() / 15;
                component.setBounds((getWidth() - buttonWidth) / 2, LanceOfDestiny.getScreenHeight()-LanceOfDestiny.getScreenHeight()/8, buttonWidth, buttonHeight);
            }
        }
    }
}
//For test purposes!
//TODO: call this when the user life == 0
/* 
    public static void main(String[] args) {
        // Example usage
        JFrame frame = new JFrame("Game Over");
        GameOverPanel panel = new GameOverPanel(true, 128); // Replace with actual values
        frame.add(panel);
        frame.setSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
} */



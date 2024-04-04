package tr.edu.ku.comp302.ui.panel;

import javax.swing.*;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.LanceController;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel {
    private Lance lance;
    private LanceController controller;
    private Image playerImage;

    public GamePanel() {
        lance = new Lance(150, 250, 100, 20);
        controller = new LanceController(lance);
        setFocusable(true);
        addKeyListener(controller);
        loadPlayerImage();
    }
    
    private void loadPlayerImage() {
        try {
            InputStream imageStream = getClass().getResourceAsStream("/tr/edu/ku/comp302/ui/assets/Player.png");
            if (imageStream == null) {
                throw new IOException("Cannot find Player.png in assets folder");
            }
            playerImage = ImageIO.read(imageStream);
            lance.setWidth(playerImage.getWidth(null));
            lance.setHeight(playerImage.getHeight(null));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int imageX = lance.getX() - lance.getWidth() / 2;
        int imageY = lance.getY() - lance.getHeight() / 2;
        g2d.translate(lance.getX(), lance.getY());
        g2d.rotate(Math.toRadians(lance.getRotationDegrees()));
        g2d.drawImage(playerImage, -lance.getWidth() / 2, -lance.getHeight() / 2, this);
    }

}

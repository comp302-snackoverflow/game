package tr.edu.ku.comp302.ui.panel;

import javax.swing.*;

import tr.edu.ku.comp302.domain.entity.*;
import tr.edu.ku.comp302.domain.handler.LanceController;
import tr.edu.ku.comp302.domain.lanceofdestiny.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class GamePanel extends JPanel {

    private Lance lance;
    private LanceController controller;
    private Image lanceImage;
    LanceOfDestiny lanceOfDestiny;
    GameMap gameMap;
    Level level;


    /**
     * This constructor sets the initial objects on the panel
     * 
     * @param lanceOfDestiny
     */
    public GamePanel(Level level) {
        this.level = level;
        gameMap = level.getCurrentMap();
        
        //lance = gameMap.getCurrentLance();
        lance = new Lance(100, 100, 200, 50);
        controller = new LanceController(lance);
        setFocusable(true);
        addKeyListener(controller);
        loadLanceImage();
    }
    
    private void loadLanceImage() {
        try {
            InputStream imageStream = getClass().getResourceAsStream("/tr/edu/ku/comp302/ui/assets/Player.png");
            if (imageStream == null) {
                throw new IOException("Cannot find Player.png in assets folder");
            }
            lanceImage = ImageIO.read(imageStream);
            lance.setWidth(lanceImage.getWidth(null));
            lance.setHeight(lanceImage.getHeight(null));
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
        g2d.drawImage(lanceImage, -lance.getWidth() / 2, -lance.getHeight() / 2, this);
    }

}

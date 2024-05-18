package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.CircularButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class LevelPanel extends JPanel {
    private LevelHandler levelHandler;
    private Graphics levelG;

    public LevelPanel(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
        setLayout(null); // Use null layout for manual positioning
        addKeyListener(new KeyboardHandler());
        addButtons();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        levelHandler.renderLance(g);
        levelHandler.renderFireBall(g);
        levelHandler.renderBarriers(g);
        levelHandler.renderRemains(g);
        levelHandler.renderHexs(g);
        levelHandler.renderSpellBox(g);
        prepareHeartImage(g);
        levelG = g;
    }

    public void setPanelSize(Dimension size) {
        System.out.println("Setting panel size: " + size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        levelHandler.getLance().adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeLanceImage();
        levelHandler.getFireBall().updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeFireBallImage();

        levelHandler.resizeBarrierImages();
        levelHandler.getBarriers().forEach(barrier -> {
            barrier.adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                    (int) size.getWidth(), (int) size.getHeight());
        });

        levelHandler.resizeRemainImage();
        levelHandler.getRemains().forEach(remain ->
            remain.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                    (int) size.getWidth(), (int) size.getHeight()));

        levelHandler.resizeSpellBoxImage();
        levelHandler.getSpellBoxes().forEach(spellBox ->
            spellBox.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(),
                    (int) size.getWidth(), (int) size.getHeight()));
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }

    public void setLevelHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
    }

    public void addButtons() {
        addCircularButton("/assets/lance_image.png", LanceOfDestiny.getScreenWidth()-100, 500, e -> {
            levelHandler.useSpell(SpellBox.EXTENSION_SPELL);
            requestFocus();
            repaint();
        });
        addCircularButton("/assets/fireball_image.png", LanceOfDestiny.getScreenWidth()-200, 500, e -> {
            levelHandler.useSpell(SpellBox.HEX_SPELL);
            requestFocus();
            repaint();
        });
        addCircularButton("/assets/frozen_barrier.png", LanceOfDestiny.getScreenWidth()-300, 500, e -> {
            ArrayList<Barrier> chosen = levelHandler.eightRandomBarriers();
            levelHandler.renderBarriers(levelG);
            requestFocus();
        });
        addCircularButton("/assets/hollow_barrier.png",LanceOfDestiny.getScreenWidth()-400, 500, e -> {
            levelHandler.generateHollowBarriers();
            requestFocus();
        });

        revalidate();
        repaint();
    }

    private void addCircularButton(String iconPath, int x, int y, ActionListener action) {
        BufferedImage icon = ImageHandler.getImageFromPath(iconPath);
        if (icon == null) {
            System.err.println("Icon not found: " + iconPath);
            return;
        }
        CircularButton button = new CircularButton(icon);
        button.setBounds(x, y, 50, 50); // Set position and size
        button.addActionListener(action);
        add(button);
    }

    public void prepareHeartImage(Graphics g) {
        BufferedImage heart = ImageHandler.getImageFromPath("/assets/heart_image.png");
        if (heart == null) {
            System.err.println("Heart image not found");
            return;
        }
    
        // Resize the heart image
        int heartWidth = 20;  // Adjust the width as needed
        int heartHeight = 20; // Adjust the height as needed
        BufferedImage resizedHeart = ImageHandler.resizeImage(heart, heartWidth, heartHeight);
    
        // Position the heart image at the bottom-left corner of the screen
        int x = 20; // Distance from the left edge
        int y = getHeight() - heartHeight - 20; // Distance from the bottom edge
    
        g.drawImage(resizedHeart, x, y, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("x" + levelHandler.getLevel().getChances(), x + heartWidth + 5, y + heartHeight / 2 + 6);
    }
    
}

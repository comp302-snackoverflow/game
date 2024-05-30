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
import java.util.List;

public class LevelPanel extends JPanel {
    private LevelHandler levelHandler;
    private Graphics levelG;
    private JLabel extensionSpellLabel;
    private JLabel hexSpellLabel;
    private final int heartWidth = 20;
    private final int heartHeight = 20;
    private final int iconSize = 40;
    private final int iconSpacing = 10;

    public LevelPanel(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
        this.levelHandler.setLevelPanel(this);
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
        prepareHeartImage(g, (int) (getWidth() * 0.05), getHeight() - heartHeight - 20);
        showYmir(g);
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
        resizeSpellIcons((int) size.getWidth(), (int) size.getHeight());
        resizeHeartImage((int) size.getWidth(), (int) size.getHeight());
    }

    public LevelHandler getLevelHandler() {
        return levelHandler;
    }

    public void setLevelHandler(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
    }

    private void addButtons() {
        extensionSpellLabel = addCircularButtonWithLabel("/assets/lance_extension.png", LanceOfDestiny.getScreenWidth() - 100, 500, e -> {
            levelHandler.useSpell(SpellBox.EXTENSION_SPELL);
            updateSpellCounts();
            requestFocus();
            repaint();
        });

        hexSpellLabel = addCircularButtonWithLabel("/assets/hex.png", LanceOfDestiny.getScreenWidth() - 100, 500 + 50, e -> {
            levelHandler.useSpell(SpellBox.HEX_SPELL);
            updateSpellCounts();
            requestFocus();
            repaint();
        });

        revalidate();
        repaint();
    }

    private JLabel addCircularButtonWithLabel(String iconPath, int x, int y, ActionListener action) {
        BufferedImage icon = ImageHandler.getImageFromPath(iconPath);
        if (icon == null) {
            System.err.println("Icon not found: " + iconPath);
            return null;
        }

        // Resize the icon
        int iconSize = 40; // Adjust size as needed
        BufferedImage resizedIcon = ImageHandler.resizeImage(icon, iconSize, iconSize);

        CircularButton button = new CircularButton(resizedIcon);
        button.setBounds(0, 0, 50, 50); // Set position and size
        button.addActionListener(action);

        JLabel label = new JLabel("0");
        label.setBounds(60, 15, 30, 20); // Adjust position relative to button

        JPanel panel = new JPanel(null); // Use null layout for custom positioning
        panel.setBounds(x, y, 100, 50); // Set panel size to fit button and label
        panel.add(button);
        panel.add(label);

        add(panel);

        return label;
    }


    public void updateSpellCounts() {
        int[] counts = SpellBox.getSpellCounts();
        extensionSpellLabel.setText(String.valueOf(counts[SpellBox.EXTENSION_SPELL]));
        hexSpellLabel.setText(String.valueOf(counts[SpellBox.HEX_SPELL]));
    }


    public void prepareHeartImage(Graphics g, int x, int y) {
        BufferedImage heart = ImageHandler.getImageFromPath("/assets/heart_image.png");
        if (heart == null) {
            System.err.println("Heart image not found");
            return;
        }

        // Resize the heart image
        BufferedImage resizedHeart = ImageHandler.resizeImage(heart, heartWidth, heartHeight);

        // Draw the heart image at the specified position
        g.drawImage(resizedHeart, x, y, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("x" + levelHandler.getLevel().getChances(), x + heartWidth + 5, y + heartHeight / 2 + 6);
    }


    public void showYmir(Graphics g){

        // Position the heart image at the bottom-left corner of the screen
        int x = 20; // Distance from the left edge
        int y = getHeight() - 20; // Distance from the bottom edgeü

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("x" + (levelHandler.getRemainingTimeForYmir()), x + 5, y + 6);

    }

    private void resizeSpellIcons(int panelWidth, int panelHeight) {
        int xOffset = panelWidth - 100;
        int yOffsetBase = (int) (panelHeight * 0.6);

        int yOffsetHex = yOffsetBase + iconSize + iconSpacing;
        extensionSpellLabel.getParent().setBounds(xOffset, yOffsetBase, 100, 50);
        hexSpellLabel.getParent().setBounds(xOffset, yOffsetHex, 100, 50);
    }

    private void resizeHeartImage(int panelWidth, int panelHeight) {
        int x = (int) (panelWidth * 0.05);
        int y = panelHeight - heartHeight - 20;
        prepareHeartImage(levelG, x, y);
    }
    
}

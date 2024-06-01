package tr.edu.ku.comp302.ui.panel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.event.KeyPressHandler;

import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.domain.listeners.MPDataListener;
import tr.edu.ku.comp302.domain.listeners.Pausable;
import tr.edu.ku.comp302.domain.listeners.PauseListener;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.CircularButton;
import tr.edu.ku.comp302.ui.view.View;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LevelPanel extends JPanel implements Pausable, MPDataListener {
    private static final Logger logger = LogManager.getLogger(LevelPanel.class);
    private static final View heartView = View.of(View.HEART);
    private final JButton pauseButton;
    private final int heartWidth = 20;
    private final int heartHeight = 20;
    private final int iconSize = 40;
    private final int iconSpacing = 10;
    private LevelHandler levelHandler;
    private JLabel extensionSpellLabel;
    private JLabel hexSpellLabel;
    private PauseListener pauseListener;
    private MainFrame mainFrame;
    private BufferedImage backgroundImage;

    public LevelPanel(LevelHandler levelHandler, MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.levelHandler = levelHandler;
        addKeyListener(new KeyboardHandler());
        setFocusable(true);
        setLayout(null);
        pauseButton = new JButton("PAUSE");

        pauseButton.addActionListener(e -> {
            if (pauseListener == null) {
                logger.warn("Pause listener is not set.");
            } else {
                pauseListener.handlePauseRequest(this);
            }
        });

        pauseButton.setBounds(0, 0, 10, 20);

        KeyPressHandler.bindKeyPressAction(this, "ESCAPE", e -> pauseButton.doClick());
        KeyPressHandler.bindKeyPressAction(this, "T", e -> levelHandler.useSpell(SpellBox.EXTENSION_SPELL));
        KeyPressHandler.bindKeyPressAction(this, "H", e -> levelHandler.useSpell(SpellBox.HEX_SPELL));

        addButtons();

        this.add(pauseButton);
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/light.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null ) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        levelHandler.renderLance(g);
        levelHandler.renderFireBall(g);
        levelHandler.renderBarriers(g);
        levelHandler.renderRemains(g);
        levelHandler.renderHexes(g);
        levelHandler.renderSpellBox(g);
        showYmir(g);
        showScore(g);
        finishGame();

        int x = (int) (getWidth() * 0.05);
        int y = getHeight() - heartHeight - 20;
        g.drawImage(heartView.getImage(), x, y, null);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("x" + levelHandler.getLevel().getChances(), x + heartWidth + 30, getHeight() - 14);
    }

    private void finishGame() {
        if (levelHandler.isFinished()) {
            mainFrame.showGameOverPanel(levelHandler.isWon(), levelHandler.getScore());
        }
    }

    private void showScore(Graphics g) {
        // Draw the score in the top right corner
        g.setColor(Color.BLACK);
        g.drawString("Score: " + levelHandler.getScore(),
                getWidth() - g.getFontMetrics().stringWidth("Score: " + levelHandler.getScore()) - 20,
                20);
    }

    public void setPanelSize(Dimension size) {
        logger.debug("Setting panel size: {}", size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);

        levelHandler.getLance().adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeLanceImage();
        levelHandler.getFireBall().updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight());
        levelHandler.resizeFireBallImage();

        levelHandler.resizeBarrierImages();
        levelHandler.getBarriers().forEach(barrier -> barrier.adjustPositionAndSize(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight()));

        levelHandler.resizeRemainImage();
        levelHandler.getRemains().forEach(remain -> remain.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight()));

        levelHandler.resizeSpellBoxImage();
        levelHandler.getSpellBoxes().forEach(spellBox -> spellBox.updatePositionRelativeToScreen(LanceOfDestiny.getScreenWidth(), LanceOfDestiny.getScreenHeight(), (int) size.getWidth(), (int) size.getHeight()));
        resizeSpellIcons((int) size.getWidth(), (int) size.getHeight());
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

    private JLabel addCircularButtonWithLabel(String iconPath, int xPosition, int yPosition, ActionListener action) {
        BufferedImage iconImage = ImageHandler.getImageFromPath(iconPath);
        if (iconImage == null) {
            throw new IllegalArgumentException("Icon not found: " + iconPath);
        }

        int buttonSize = 40;
        BufferedImage resizedIconImage = ImageHandler.resizeImage(iconImage, buttonSize, buttonSize);

        CircularButton button = new CircularButton(resizedIconImage);
        button.setBounds(0, 0, buttonSize, buttonSize);
        button.addActionListener(action);

        JLabel spellCountLabel = new JLabel("0");
        spellCountLabel.setBounds(buttonSize + 10, 0, 30, 20);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.setBounds(xPosition, yPosition, buttonSize + 40, buttonSize);
        buttonPanel.add(button);
        buttonPanel.add(spellCountLabel);
        buttonPanel.setOpaque(false);

        add(buttonPanel);

        buttonPanel.setVisible(true);

        return spellCountLabel;
    }

    public void updateSpellCounts() {
        int[] counts = SpellBox.getSpellCounts();
        extensionSpellLabel.setText(String.valueOf(counts[SpellBox.EXTENSION_SPELL]));
        hexSpellLabel.setText(String.valueOf(counts[SpellBox.HEX_SPELL]));
    }

    public void showYmir(Graphics g){
        // Position the heart image at the bottom-left corner of the screen
        int x = 20; // Distance from the left edge
        int y = getHeight() - 20; // Distance from the bottom edgeü

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("x" + levelHandler.getRemainingTimeForYmir(), x + 5, y + 6);
    }

    private void resizeSpellIcons(int panelWidth, int panelHeight) {
        int xOffset = panelWidth - 100;
        int yOffsetBase = (int) (panelHeight * 0.6);

        int yOffsetHex = yOffsetBase + iconSize + iconSpacing;
        extensionSpellLabel.getParent().setBounds(xOffset, yOffsetBase, 100, 50);
        hexSpellLabel.getParent().setBounds(xOffset, yOffsetHex, 100, 50);
    }



    public void changeBackgroundToYmir(){
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/dark.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeBackgroundToOrginal(){
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/light.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setPauseListener(PauseListener listener) {
        this.pauseListener = listener;
    }

    public void pause() {
        mainFrame.showPausePanel();
    }

    public void handleData(String data) {
        String[] parts = data.split(":");
        if (parts[0].equals("DATA")) {
            int score = Integer.parseInt(parts[1]);
            int chances = Integer.parseInt(parts[2]);
            int barriersCount = Integer.parseInt(parts[3]);
            System.out.println("Score: " + score + " Chances: " + chances + " Barriers: " + barriersCount);
            // TODO: complete this method
        } else if (parts[0].equals("INFO")) {
            String info = parts[1];
            System.out.println("Other player: " + info);
        }
    }
}

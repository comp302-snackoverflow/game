package tr.edu.ku.comp302.ui.panel;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;
import tr.edu.ku.comp302.domain.handler.LevelHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;


public class LevelPanel extends JPanel {
    private LevelHandler levelHandler;
    private LanceOfDestiny lanceOfDestiny;

    public LevelPanel(LevelHandler levelHandler) {
        this.levelHandler = levelHandler;
        addKeyListener(new KeyboardHandler());
        addButtons();
    }

    /**
     * Overrides the paintComponent method from JPanel to render the components
     * in the levelHandler.
     *
     * @param g The Graphics object used for rendering.
     */
    public void paintComponent(Graphics g){
        // Call the paintComponent method of the superclass
        super.paintComponent(g);

        // Render the lance using the levelHandler
        levelHandler.renderLance(g);

        // Render the fireball using the levelHandler
        levelHandler.renderFireBall(g);

        // Render the barriers using the levelHandler
        levelHandler.renderBarriers(g);

        // Render the remains of destroyed ExplosiveBarriers using the levelHandler
        levelHandler.renderRemains(g);


        levelHandler.renderHexs(g);

        levelHandler.renderSpellBox(g);
        prepareHeartImage(g);

    }

    // TODO: Handle this method later.
    public void setPanelSize(Dimension size){
        System.out.println("Setting panel size: " + size);
        setMinimumSize(size);
        setPreferredSize(size);
        setMaximumSize(size);
        
        // TODO: Add other entities
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

    public void addButtons(){

        this.add(createLanceExtensionButton());
        this.add(createHexPUButton());
        
        //TODO: add more buttons for other spelss as overwhelming fireball, etc.

        

    

    }

    public JButton createLanceExtensionButton() {
        JButton button = new JButton("Lance Extension");
        double screenWidth = LanceOfDestiny.getScreenWidth();
        double screenHeight = LanceOfDestiny.getScreenHeight();
        int buttonWidth = 200;
        int buttonHeight = 40;
        int buttonX = (int) (screenWidth / 2 - buttonWidth / 2);
        int buttonY = (int) (screenHeight * 0.8 - (2 * buttonHeight) );
        button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                lanceOfDestiny.extendLance();
                
                requestFocus();
                repaint();
            }
        });
        return button;
    }


    public JButton createHexPUButton() {
        JButton button = new JButton("Use Hex Extension");
        double screenWidth = LanceOfDestiny.getScreenWidth();
        double screenHeight = LanceOfDestiny.getScreenHeight();
        int buttonWidth = 200;
        int buttonHeight = 40;
        int buttonX = (int) (screenWidth / 2 + buttonWidth / 2);
        int buttonY = (int) (screenHeight * 0.8 - (2 * buttonHeight) );
        button.setBounds(buttonX, buttonY, buttonWidth, buttonHeight);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                levelHandler.startCreatingHex();
                
                requestFocus();
                repaint();
            }
        });
        return button;
    }

    //TODO: The heart is not resizing for some reason. Fix this when you can.
    public void prepareHeartImage(Graphics g) {
        BufferedImage heart = ImageHandler.getImageFromPath("/assets/heart_image.png");
        assert heart != null;
        ImageHandler.resizeImage(heart, LanceOfDestiny.getScreenWidth()/100,10);
        int x = getWidth() - heart.getWidth() - 40;
        int y = 5;
        g.drawImage(heart, x, y, null);
        g.setColor(Color.BLACK); // You can change the color as needed
        g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("x" + levelHandler.getLevel().getChances(), x + heart.getWidth() + 5, y + heart.getHeight() / 2 + 6);
    }


    public void setLanceOfDestiny(LanceOfDestiny lanceOfDestiny) {
        this.lanceOfDestiny = lanceOfDestiny;
    }
}

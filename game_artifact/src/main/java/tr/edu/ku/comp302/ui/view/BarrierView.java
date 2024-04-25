package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;
import tr.edu.ku.comp302.domain.entity.Barriers.FirmBarrier;
import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BarrierView {
    private Barrier barrier;
    private BufferedImage barrierImage;

    

    public BarrierView(Barrier barrier) {
        this.barrier = barrier;
        setBarrierImage();
        
    }

    public void render(Graphics g) {
        g.drawImage(barrierImage, (int) barrier.getXPosition(), (int) barrier.getYPosition(), null);
        if (barrier instanceof FirmBarrier) {
            int health = ((FirmBarrier) barrier).getHealth();
            
            // Define the position for displaying the health text
            int textX = (int) barrier.getXPosition() + barrierImage.getWidth(null) / 2; 
            int textY = (int) barrier.getYPosition() - 3; //3 pixels above the barrier 
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 8));
            g.drawString(String.valueOf(health), textX, textY);
        }
    }

    public void setBarrierImage() {
        barrierImage = ImageHandler.getImageFromPath(barrier.getImagePath());
    }

    public void setBarrierImage(BufferedImage image) {
        barrierImage = image;
    }

    public Barrier getBarrier() {
        return barrier;
    }

    public BufferedImage getBarrierImage() {
        return barrierImage;
    }
}

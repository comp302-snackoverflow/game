package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.Barrier;
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
    }

    public void setBarrierImage() {
        barrierImage = ImageHandler.getImageFromPath("/assets/barrier_image.png");
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

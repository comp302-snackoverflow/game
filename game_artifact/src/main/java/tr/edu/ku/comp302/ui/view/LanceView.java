package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.ImageHandler;
import tr.edu.ku.comp302.domain.handler.KeyboardHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class LanceView {
    private Lance lance;
    private BufferedImage lanceImage;
    public LanceView(Lance lance) {
        this.lance = lance;
        setLanceImage();    // May change
    }
    public void render(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        double rotationAngle = Math.toRadians(getLance().getRotationAngle());
        // AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(rotationAngle, lance.getXPosition() + lance.getLength() / 2.0, lance.getYPosition() + lance.getThickness() / 2.0);
        g2d.drawImage(lanceImage, (int) lance.getXPosition(), (int) lance.getYPosition(), null);
    }

    public void moveLance(int px){
        if (KeyboardHandler.leftArrowPressed && !KeyboardHandler.rightArrowPressed){
            lance.updateXPosition(-px);
        }else if (!KeyboardHandler.leftArrowPressed && KeyboardHandler.rightArrowPressed){
            lance.updateXPosition(px);
        }
        // TODO: Add left or right arrow tap case in here.
    }

    public void rotateLance(double degrees){
        lance.incrementRotationAngle(degrees);
    }

    public Lance getLance() {
        return lance;
    }

    public void setLance(Lance lance) {
        this.lance = lance;
    }

    public BufferedImage getLanceImage() {
        return lanceImage;
    }

    public void setLanceImage() {
        lanceImage = ImageHandler.getImageFromPath("/assets/lance_image.png");
    }
    public void setLanceImage(BufferedImage lanceImage) {
        this.lanceImage = lanceImage;
    }


}

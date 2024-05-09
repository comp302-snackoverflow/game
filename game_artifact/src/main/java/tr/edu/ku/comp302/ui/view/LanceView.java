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
        AffineTransform oldTransform = g2d.getTransform();
        g2d.rotate(rotationAngle, lance.getXPosition() + lance.getLength() / 2.0, lance.getYPosition() + lance.getThickness() / 2.0);
        g2d.drawImage(lanceImage, (int) lance.getXPosition(), (int) lance.getYPosition(), null);
        g2d.setTransform(oldTransform);    // Reset transformation to prevent unintended rotations.

        // uncomment the below two lines to draw Lance Hitbox and Lance Bounding Box

        g2d.drawPolygon(lance.getActualHitbox());
        ((Graphics2D) g).draw(lance.getLanceBounds());

        // uncomment below lines to draw a desired Side Line of the Lance Hitbox
        // change the indexes for different sides
        // index zero is the top left corner, rest of them are in clock-wise order

        //g.drawLine(lance.getActualHitbox().xpoints[0], lance.getActualHitbox().ypoints[0],
        //        lance.getActualHitbox().xpoints[3], lance.getActualHitbox().ypoints[3]);
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


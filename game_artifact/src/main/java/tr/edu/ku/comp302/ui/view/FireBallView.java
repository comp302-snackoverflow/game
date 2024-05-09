package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FireBallView {
    private FireBall fireBall;

    private BufferedImage fireBallImage;

    public FireBallView(FireBall fireBall) {
        this.fireBall = fireBall;
        setFireBallImage();
    }

    public void render(Graphics g) {
        g.drawImage(fireBallImage, (int) fireBall.getXPosition(), (int) fireBall.getYPosition(), null);
        // uncomment the below line to see FireBall hit box
        g.drawRect((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
    }

    public void setFireBallImage() {
        this.fireBallImage = ImageHandler.getImageFromPath("/assets/fireball_image.png");
    }

    public FireBall getFireBall() {
        return fireBall;
    }

    public void setFireBall(FireBall fireBall) {
        this.fireBall = fireBall;
    }

    public void setFireBallImage(BufferedImage fireBallImage) {
        this.fireBallImage = fireBallImage;
    }

    public BufferedImage getFireBallImage() {
        return fireBallImage;
    }
}



package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FireBallView {
    private BufferedImage fireBallImage;

    public FireBallView() {
        setFireBallImage();
    }

    public void setFireBallImage() {
        this.fireBallImage = ImageHandler.getImageFromPath("/assets/fireball_image.png");
    }

    public void setFireBallImage(BufferedImage fireBallImage) {
        this.fireBallImage = fireBallImage;
    }

    public BufferedImage getFireBallImage() {
        return fireBallImage;
    }
}



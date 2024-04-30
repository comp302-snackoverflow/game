package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class LanceView {
    private BufferedImage lanceImage;

    public LanceView() {
        setLanceImage();
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


package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.Remains;
import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RemainsView {
    private Remains remains;

    private BufferedImage remainsImage;

    public RemainsView(Remains remains){
        this.remains = remains;
        setRemainsImage();
        
    }

    public void render(Graphics g){
        g.drawImage(remainsImage, (int) remains.getXPosition(), (int) remains.getYPosition(), null);
    }

    public Remains getRemains() {
        return remains;
    }

    public void setRemains(Remains remains) {
        this.remains = remains;
    }

    public BufferedImage getRemainsImage() {
        return remainsImage;
    }

    public void setRemainsImage(BufferedImage remainsImage) {
        this.remainsImage = remainsImage;
    }

    public void setRemainsImage() {
        remainsImage = 
        ImageHandler.resizeImage(
                    ImageHandler.getImageFromPath("/assets/remains_image.png"),
                    getRemains().getSize(),
                    getRemains().getSize()
            );
    }
}

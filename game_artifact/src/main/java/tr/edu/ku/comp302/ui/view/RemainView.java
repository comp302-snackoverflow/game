package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.image.BufferedImage;

public class RemainView {

    private BufferedImage remainImage;

    public RemainView(){
        setRemainImage();
        
    }

    public BufferedImage getRemainImage() {
        return remainImage;
    }

    public void setRemainImage(BufferedImage remainImage) {
        this.remainImage = remainImage;
    }

    public void setRemainImage() {
        remainImage = ImageHandler.getImageFromPath("/assets/remain_image.png");
    }
}

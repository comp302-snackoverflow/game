package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BarrierView {
    private BufferedImage barrierImage;

    public BarrierView(String barrierType) {
        setBarrierImage(barrierType);
    }

    public void setBarrierImage(String barrierType) {
        ImageHandler.getImageFromPath("/assets/" + barrierType + "_barrier.png");
    }

    public void setBarrierImage(BufferedImage image) {
        barrierImage = image;
    }

    public BufferedImage getBarrierImage() {
        return barrierImage;
    }
}

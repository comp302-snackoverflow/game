package tr.edu.ku.comp302.domain.handler;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageHandler {
    public static BufferedImage createCustomImage(String pathStr, int widthNew, int heightNew){
        BufferedImage image = getImageFromPath(pathStr);
        assert image != null;
        return resizeImage(image, widthNew, heightNew);
    }

    public static BufferedImage resizeImage(BufferedImage image, int widthNew, int heightNew){
        BufferedImage resizedImage = new BufferedImage(widthNew, heightNew, image.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, widthNew, heightNew, null);
        g2d.dispose();
        return resizedImage;
    }

    public static BufferedImage getImageFromPath(String pathStr){
        InputStream is = ImageHandler.class.getResourceAsStream(pathStr);
        try {
            assert is != null;
            return ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();    // TODO: Change this later
            return null;
        }
    }
}


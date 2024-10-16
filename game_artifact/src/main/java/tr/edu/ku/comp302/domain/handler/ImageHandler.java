package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class ImageHandler {
    private static final Logger logger = LogManager.getLogger(ImageHandler.class);

    public static BufferedImage resizeImage(BufferedImage image, int widthNew, int heightNew) {
        if (widthNew == 0) {
            resizeImage(image, 1, heightNew);
        }
        if (heightNew == 0) {
            resizeImage(image, widthNew, 1);
        }
        BufferedImage resizedImage = new BufferedImage(widthNew, heightNew, image.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.drawImage(image, 0, 0, widthNew, heightNew, null);
        g2d.dispose();
        return resizedImage;
    }

    public static BufferedImage getImageFromPath(String pathStr) {
        logger.debug("Attempting to load image from path: " + pathStr);
        InputStream is = ImageHandler.class.getResourceAsStream(pathStr);
        if (is == null) {
            logger.error("Image not found at path: " + pathStr);
            return null;
        }
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            logger.error("Error reading image at path: " + pathStr, e);
            return null;
        }
    }
}

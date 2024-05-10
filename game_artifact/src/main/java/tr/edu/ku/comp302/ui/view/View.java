package tr.edu.ku.comp302.ui.view;

import tr.edu.ku.comp302.domain.handler.ImageHandler;

import java.awt.image.BufferedImage;

public class View {
    public enum Type {
        FIREBALL,
        LANCE,
        SIMPLE_BARRIER,
        FIRM_BARRIER,
        EXPLOSIVE_BARRIER,
        REMAIN,
        GIFT_BARRIER,
        LANCE_EXTENSION,
        MISSING_TEXTURE,
    }

    public static final Type FIREBALL = Type.FIREBALL;
    public static final Type LANCE = Type.LANCE;
    public static final Type SIMPLE_BARRIER = Type.SIMPLE_BARRIER;
    public static final Type FIRM_BARRIER = Type.FIRM_BARRIER;
    public static final Type EXPLOSIVE_BARRIER = Type.EXPLOSIVE_BARRIER;
    public static final Type REMAIN = Type.REMAIN;
    public static final Type GIFT_BARRIER = Type.GIFT_BARRIER;
    public static final Type LANCE_EXTENSION = Type.LANCE_EXTENSION;
    public static final Type MISSING_TEXTURE = Type.MISSING_TEXTURE;

    private static final String FIREBALL_IMAGE_PATH = "/assets/fireball_image.png";
    private static final String LANCE_IMAGE_PATH = "/assets/lance_image.png";
    private static final String SIMPLE_BARRIER_IMAGE_PATH = "/assets/simple_barrier.png";
    private static final String FIRM_BARRIER_IMAGE_PATH = "/assets/firm_barrier.png";
    private static final String EXPLOSIVE_BARRIER_IMAGE_PATH = "/assets/explosive_barrier.png";
    private static final String REMAIN_IMAGE_PATH = "/assets/remain_image.png";
    private static final String GIFTING_BARRIER_IMAGE_PATH = "/assets/gifting_barrier.png";
    private static final String LANCE_EXTENSION_IMAGE_PATH = "/assets/lance_extension.png";
    private static final String MISSING_TEXTURE_IMAGE_PATH = "/assets/missing_texture.png";

    private BufferedImage defaultImage;
    private BufferedImage image;

    private View(String imagePath) {
        defaultImage = ImageHandler.getImageFromPath(imagePath);
        image = defaultImage;
    }

    public BufferedImage resizeImage(int width, int height) {
        image = ImageHandler.resizeImage(defaultImage, width, height);
        return image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public static View of(Type type) {
        return switch (type) {
            case FIREBALL -> new View(FIREBALL_IMAGE_PATH);
            case LANCE -> new View(LANCE_IMAGE_PATH);
            case SIMPLE_BARRIER -> new View(SIMPLE_BARRIER_IMAGE_PATH);
            case FIRM_BARRIER -> new View(FIRM_BARRIER_IMAGE_PATH);
            case EXPLOSIVE_BARRIER -> new View(EXPLOSIVE_BARRIER_IMAGE_PATH);
            case REMAIN -> new View(REMAIN_IMAGE_PATH);
            case GIFT_BARRIER -> new View(GIFTING_BARRIER_IMAGE_PATH);
            case LANCE_EXTENSION -> new View(LANCE_EXTENSION_IMAGE_PATH);
            case MISSING_TEXTURE -> new View(MISSING_TEXTURE_IMAGE_PATH);
        };
    }
}

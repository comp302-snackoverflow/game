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
        HEX,
        SPELL_BOX,
        FROZEN_BARRIER,
        HOLLOW_BARRIER,
        OVERWHELMED_FIREBALL,
        HEART,
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
    public static final Type HEX = Type.HEX;
    public static final Type SPELL_BOX = Type.SPELL_BOX;
    public static final Type FROZEN_BARRIER = Type.FROZEN_BARRIER;
    public static final Type HOLLOW_BARRIER = Type.HOLLOW_BARRIER;
    public static final Type OVERWHELMED_FIREBALL = Type.OVERWHELMED_FIREBALL;
    public static final Type HEART = Type.HEART;

    private static final String HEX_IMAGE_PATH = "/assets/fireball_image.png";
    private static final String FIREBALL_IMAGE_PATH = "/assets/fireball_image.png";
    private static final String LANCE_IMAGE_PATH = "/assets/lance_image.png";
    private static final String SIMPLE_BARRIER_IMAGE_PATH = "/assets/simple_barrier.png";
    private static final String FIRM_BARRIER_IMAGE_PATH = "/assets/firm_barrier.png";
    private static final String EXPLOSIVE_BARRIER_IMAGE_PATH = "/assets/explosive_barrier.png";
    private static final String REMAIN_IMAGE_PATH = "/assets/remain_image.png";
    private static final String GIFTING_BARRIER_IMAGE_PATH = "/assets/gift_barrier.png";
    private static final String LANCE_EXTENSION_IMAGE_PATH = "/assets/lance_extension.png";
    private static final String MISSING_TEXTURE_IMAGE_PATH = "/assets/missing_texture.png";
    private static final String SPELL_BOX_IMAGE_PATH = "/assets/spell_box.png";
    private static final String FROZEN_BARRIER_PATH = "/assets/frozen_barrier.png";
    private static final String HOLLOW_BARRIER_PATH = "/assets/hollow_barrier.png";
    private static final String OVERHWELM_FIREBALL_PATH = "/assets/overwhelming_fireball.png";
    private static final String HEART_PATH = "/assets/heart_image.png";

    private final BufferedImage defaultImage;
    private BufferedImage image;

    private View(String imagePath) {
        defaultImage = ImageHandler.getImageFromPath(imagePath);
        image = defaultImage;
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
            case HEX -> new View(HEX_IMAGE_PATH);
            case SPELL_BOX -> new View(SPELL_BOX_IMAGE_PATH);
            case FROZEN_BARRIER -> new View(FROZEN_BARRIER_PATH);
            case HOLLOW_BARRIER -> new View(HOLLOW_BARRIER_PATH);
            case OVERWHELMED_FIREBALL -> new View(OVERHWELM_FIREBALL_PATH);
            case MISSING_TEXTURE -> new View(MISSING_TEXTURE_IMAGE_PATH);
            case HEART -> new View(HEART_PATH);

        };
    }

    public BufferedImage resizeImage(int width, int height) {
        image = ImageHandler.resizeImage(defaultImage, width, height);
        return image;
    }

    public BufferedImage getImage() {
        return image;
    }
}

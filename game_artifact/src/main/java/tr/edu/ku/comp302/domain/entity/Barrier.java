package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

public class Barrier extends Entity {
    private static final double DEFAULT_THICKNESS = 20;
    private static final int DEFAULT_HEALTH = 1;
    private static final double DEFAULT_SPEED = 0;
    private int health;
    private double length;
    private double thickness;
    private double speed;
    private double L;

    public Barrier(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        thickness = DEFAULT_THICKNESS;
        speed = DEFAULT_SPEED;
        health = DEFAULT_HEALTH;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
    }

    public void setL(double l) {
        L = l;
        length = L / 5;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
    }
    @Override
    public void handleCollision(boolean isWall) {
        health--;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getLength() {
        return length;
    }
    public double getThickness() {
        return thickness;
    }

}


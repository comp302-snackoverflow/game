package tr.edu.ku.comp302.domain.entity.Barriers;

import java.awt.geom.Rectangle2D;

import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.BarrierBehaviors.CollisionBehaviours.CollisionBehavior;

public class Barrier extends Entity {
    protected static final double DEFAULT_THICKNESS = 20;
    protected static final int DEFAULT_HEALTH = 1;
    protected static final double DEFAULT_SPEED = 0;
    protected int health;
    protected double length;
    protected double thickness;
    protected double speed;
    protected double L;
    String ImagePath;



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
        //CollisionBehavior.handleCollision(this);
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

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    

}

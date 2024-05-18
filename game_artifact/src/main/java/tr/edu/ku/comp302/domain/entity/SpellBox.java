package tr.edu.ku.comp302.domain.entity;
import java.awt.geom.Rectangle2D;

public class SpellBox extends Entity {
    private double speed = 1.5;     // TODO: Change speed
    private int size = 50;
    private boolean isDropped = false;
    //TODO Add spell attribute 

    public SpellBox(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }
    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void move() {
        yPosition += speed;
        boundingBox.setRect(xPosition, yPosition, size, size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDropped() {
        return isDropped;
    }

    public void drop() {
        isDropped = true;
    }
}


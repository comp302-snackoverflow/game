package tr.edu.ku.comp302.domain.entity;
import java.awt.geom.Rectangle2D;
import java.util.Random;

public class SpellBox extends Entity {
    private double speed = 1.5;     // TODO: Change speed
    private int size = 50;
    private boolean isDropped = false;
    //TODO Add spell attribute 
    private char spell = 0;
    // 0 is lance extension spell
    // 1 is fireball spell
    // 2 is shield spell

    
    public SpellBox(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        spell = (char) ((new Random()).nextInt(3) ) ;
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


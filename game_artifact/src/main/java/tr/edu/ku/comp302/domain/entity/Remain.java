package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

public class Remain extends Entity{
    
    private double speed = 1.5;     // TODO: Change speed
    private int size = 50;


    public Remain(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
        actualShape = boundingBox;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    // TODO: collision with lance to be added

    @Override
    public void handleCollision(boolean isWall) {
        System.out.println("Remains collided with lance");
//        throw new UnsupportedOperationException("Unimplemented method 'handleCollision'");
    }

    public void move() {
        yPosition += speed;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
        actualShape = boundingBox;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

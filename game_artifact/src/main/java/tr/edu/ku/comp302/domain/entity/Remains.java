package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

public class Remains extends Entity{
    
    private double speed = 1.5;
    private int size = 50;


    public Remains(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
        actualShape = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    //collision with lance to be added

    @Override
    public void handleCollision(boolean isWall) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleCollision'");
    }

    public void move() {
        yPosition += speed;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}

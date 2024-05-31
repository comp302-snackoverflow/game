package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class Hex extends Entity {
    private int size = 16;
    private double rotationAngle;
    private double speed;

    public Hex(double xPosition, double yPosition, double rotationAngle) {
        super(xPosition, yPosition);
        this.rotationAngle = rotationAngle;
        speed = LanceOfDestiny.getScreenWidth() * 0.2;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }

    /**
     * Moves the hex by rotating around its center and updating its position
     * and bounding box.
     */
    public void move(double speed) {
        // calculate the changes in x and y based on the rotation angle
        double yChange = speed * Math.cos(Math.toRadians(rotationAngle)) ;
        double xChange = speed * Math.sin(Math.toRadians(rotationAngle)) ;

        // update the hex's position
        setYPosition(yPosition - yChange);
        setXPosition(xPosition + xChange);

        // update the bounding box to reflect the new position
        boundingBox.setRect(xPosition, yPosition, size, size);
    }

    public double getSpeed() {
        return speed;
    }
}

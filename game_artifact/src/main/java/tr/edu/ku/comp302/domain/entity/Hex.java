package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;

public class Hex extends Entity {

    int size = 16;
    double rotationAngle = 0.0;

    public Hex(double xPosition, double yPosition, double rotationAngle) {
        super(xPosition, yPosition);
        //TODO Auto-generated constructor stub
        this.rotationAngle = rotationAngle;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }
   

    /**
     * Moves the hex by rotating around its center and updating its position
     * and bounding box.
     */
    public void move(){

        // calculate the changes in x and y based on the rotation angle
        double yChange = Math.cos(Math.toRadians(rotationAngle)) ;
        double xChange = Math.sin(Math.toRadians(rotationAngle)) ;

        // update the hex's position
        setYPosition(yPosition - yChange);
        setXPosition(xPosition + xChange);

        // update the bounding box to reflect the new position
        boundingBox.setRect(xPosition, yPosition, size, size);
    }
    

    
}

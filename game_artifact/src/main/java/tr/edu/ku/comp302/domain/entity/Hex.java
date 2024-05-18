package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;

public class Hex extends Entity {

    int size = 16;

    public Hex(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        //TODO Auto-generated constructor stub
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }
   

    public void move(){
        setYPosition(yPosition-1);
        boundingBox.setRect(xPosition, yPosition, size, size);
    }
    

    public boolean isCollidingWith(Barrier other) {
        return this.boundingBox.intersects(other.boundingBox);
    }
    
}

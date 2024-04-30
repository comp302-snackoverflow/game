package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

/**
 * This is the simple barrier with one hit and no exceptional property
 * 
 */
public class SimpleBarrier extends Barrier {
    public static final String TYPE = "simple";

    public SimpleBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        health = 1;
        this.MovementStrategy = new HorizontalMovement(this);
    }
    
}

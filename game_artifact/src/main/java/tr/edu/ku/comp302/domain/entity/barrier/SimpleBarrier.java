package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

public class SimpleBarrier extends Barrier {
    public SimpleBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        health = 1;
        this.movementStrategy = new HorizontalMovement();
    }
}

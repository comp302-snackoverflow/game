package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

public class FirmBarrier extends Barrier {
    public static final String TYPE = "firm";

    public FirmBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        health = random.nextInt(4) + 2;
        movementStrategy = new HorizontalMovement();
    }
}


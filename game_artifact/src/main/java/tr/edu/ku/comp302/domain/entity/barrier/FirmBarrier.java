package tr.edu.ku.comp302.domain.entity.barrier;
import java.util.Random;

import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

public class FirmBarrier extends Barrier {
    public static final String TYPE = "firm";

    public FirmBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        Random rand = new Random();
        health = rand.nextInt(4) + 2;
        movementStrategy = new HorizontalMovement(this);
    }
}


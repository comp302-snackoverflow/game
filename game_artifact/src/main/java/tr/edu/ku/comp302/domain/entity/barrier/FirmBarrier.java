package tr.edu.ku.comp302.domain.entity.barrier;
import java.util.Random;

import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

public class FirmBarrier extends Barrier {
    public static final String TYPE = "Firm";

    public FirmBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        //TODO Auto-generated constructor stub
        ImagePath = "/assets/firm_barrier.png";
        Random rand = new Random();
        health = rand.nextInt(4) + 2;

        MovementStrategy = new HorizontalMovement(this);
    }
}


package tr.edu.ku.comp302.domain.entity.Barriers;
import java.util.Random;

import tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies.HorizontalMovement;

public class FirmBarrier extends Barrier {
    public static final String TYPE = "Firm";

    public FirmBarrier(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        //TODO Auto-generated constructor stub
        ImagePath = "/assets/firm_barrier.png";
        Random rand = new Random();
        health = rand.nextInt(4) + 2;

        MovementStrategy = new HorizontalMovement(screenWidth, screenHeight, this);
    }
}


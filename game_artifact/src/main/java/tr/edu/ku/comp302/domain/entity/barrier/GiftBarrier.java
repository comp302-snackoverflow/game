package tr.edu.ku.comp302.domain.entity.barrier;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

public class GiftBarrier extends Barrier{
    public static final String TYPE= "gift";
    public GiftBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        health = 1;
        this.movementStrategy = new HorizontalMovement();
        };
    }


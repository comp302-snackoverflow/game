package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.HorizontalMovement;

public class ExplosiveBarrier extends Barrier {
    public static final String TYPE = "explosive";
    private final Remain remain;

    public ExplosiveBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        remain = new Remain(0, 0);
        health = 1;
        // FIXME: replace with circular movement once it is refactored
        this.movementStrategy = new HorizontalMovement(); // new CircularMovement();
    }

    public void dropRemains() {
        // barrier is removed then the asset remains added 
        // starting from the x and y position fot he center of 
        // the barrier that was destroyed the remains fall with constant speed
        remain.setXPosition(getXPosition() + getLength() / 2);
        remain.setYPosition(getYPosition() + getThickness() / 2);
        remain.drop();
    }

    public Remain getRemain() {
        return remain;
    }
}

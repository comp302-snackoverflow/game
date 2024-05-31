package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.CircularMovement;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class ExplosiveBarrier extends Barrier {
    private final Remain remain;

    public ExplosiveBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        remain = new Remain(0, 0);
        health = 1;
        this.movementStrategy = new CircularMovement(this);
    }

    public void dropRemains() {
        remain.setXPosition(getXPosition() + getLength() / 2);
        remain.setYPosition(getYPosition() + getThickness() / 2);
        remain.drop();
    }

    public Remain getRemain() {
        return remain;
    }

    @Override
    public double getSpeed() {
        return LanceOfDestiny.getScreenWidth() * 0.075 * Math.PI;
    }
}

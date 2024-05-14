package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.security.SecureRandom;
import java.util.List;

/**
 * This strategy class calculates the movements and collisions of the horizontal moving barriers
 */
public class HorizontalMovement implements IMovementStrategy {
    private int direction;
    private final SecureRandom random;

    public HorizontalMovement() {
        direction = 0;
        random = new SecureRandom();
    }
    @Override
    public double getXPadding() {
        return LanceOfDestiny.getScreenWidth() / 52.0;
    }

    @Override
    public double getYPadding() {
        return LanceOfDestiny.getScreenHeight() / 52.0;
    }

    /**
     * If the barrier is not stiff and the determined direction for movement is left,
     * moves with a speed of L/4 to the left, otherwise, the same logic applies to the right.
     */
    @Override
    public void move(Barrier barrier, double speed) {
        barrier.setXPosition(barrier.getXPosition() + speed * direction);
        barrier.getBoundingBox().setRect(barrier.getXPosition() + speed * direction, barrier.getYPosition(), barrier.getLength(), barrier.getThickness());
    }

    @Override
    public void turnBack() {
        direction = -direction;
    }
    @Override
    public void handleCloseCalls(Barrier barrier, List<Barrier> barriers) {
        int sides = CollisionHandler.checkCloseCalls(barrier, barriers,
                getXPadding(), getYPadding()); // the bit order is `lbrt`

        if ((sides & 0b1010) == 0b1010) {
            barrier.stopMoving();
        } else if ((direction == 1 && (sides & 0b0010) != 0)
                || (direction == -1 && (sides & 0b1000) != 0)) {
            barrier.turnBack();
        }
    }

    @Override
    public int getXDirection() {
        return direction;
    }

    @Override
    public int getYDirection() {
        return 0;
    }

    @Override
    public void stopMoving() {
        direction = 0;
    }

    @Override
    public void startMoving() {
        direction = random.nextInt(2) * 2 - 1; // -1 if nextInt is 0 else 1.
    }

    @Override
    public void adjustMovementParameters(Barrier barrier) {
        // no need to adjust anything
    }
}

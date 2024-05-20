package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.security.SecureRandom;
import java.util.List;

public class CircularMovement implements IMovementStrategy {
    private static final double INITIAL_ANGLE = Math.PI / 2;
    private static final Logger logger = LogManager.getLogger(CircularMovement.class);
    private final SecureRandom random;
    private Direction direction = Direction.STIFF;
    private double angle;
    private double radius;
    private double centerX;
    private double centerY;

    public CircularMovement(Barrier barrier) {
        this(barrier, INITIAL_ANGLE);
    }

    public CircularMovement(Barrier barrier, double angle) {
        this.angle = angle;
        radius = LanceOfDestiny.getScreenWidth() * 0.15; // L/10 * 1.5 = 1.5L
        centerX = barrier.getXPosition() + barrier.getLength() / 2 - radius * Math.cos(angle);
        centerY = barrier.getYPosition() + barrier.getThickness() / 2 + radius * Math.sin(angle);
        random = new SecureRandom();
    }

    @Override
    public double getXPadding() {
        return LanceOfDestiny.getScreenWidth() / 50.0;
    }

    @Override
    public double getYPadding() {
        return LanceOfDestiny.getScreenHeight() / 20.0;
    }

    @Override
    public void move(Barrier barrier, double speed) {
        if (direction == Direction.STIFF) {
            return;
        }

        double circumference = 2 * Math.PI * radius;
        double deltaAngle = speed / circumference;

        switch (direction) {
            case CLOCKWISE -> angle -= deltaAngle;
            case COUNTER_CLOCKWISE -> angle += deltaAngle;
        }
        if (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        } else if (angle < 0) {
            angle += 2 * Math.PI;
        }

        double newX = centerX + radius * Math.cos(angle) - barrier.getLength() / 2;
        double newY = centerY - radius * Math.sin(angle) - barrier.getThickness() / 2;

        barrier.setXPosition(newX);
        barrier.setYPosition(newY);
        barrier.getBoundingBox().setRect(newX, newY, barrier.getLength(), barrier.getThickness());

    }

    @Override
    public int getXDirection() {
        int xDirection = 0;
        if (Math.PI * 0.25 <= angle && angle <= Math.PI * 0.75) {
            xDirection = 1;
        } else if (Math.PI * 1.25 <= angle && angle <= Math.PI * 1.75) {
            xDirection = -1;
        }

        return switch (direction) {
            case CLOCKWISE -> xDirection;
            case COUNTER_CLOCKWISE -> -xDirection;
            case STIFF -> 0;
        };
    }

    @Override
    public int getYDirection() {
        int yDirection = 0;
        if (angle <= 0.25 * Math.PI || angle >= 1.75 * Math.PI) {
            yDirection = 1;
        } else if (0.75 * Math.PI < angle && angle < 1.25 * Math.PI) {
            yDirection = -1;
        }

        return switch (direction) {
            case CLOCKWISE -> yDirection;
            case COUNTER_CLOCKWISE -> -yDirection;
            case STIFF -> 0;
        };
    }

    @Override
    public void turnBack() {
        switch (direction) {
            case CLOCKWISE -> direction = Direction.COUNTER_CLOCKWISE;
            case COUNTER_CLOCKWISE -> direction = Direction.CLOCKWISE;
        }
    }

    @Override
    public void stopMoving() {
        direction = Direction.STIFF;
    }

    @Override
    public void startMoving() {
        int dir = random.nextInt(2) * 2 - 1; // -1 if nextInt is 0 else 1.
        switch (dir) {
            case -1 -> direction = Direction.CLOCKWISE;
            case 1 -> direction = Direction.COUNTER_CLOCKWISE;
        }
    }

    @Override
    public void handleCloseCalls(Barrier barrier, List<Barrier> barriers) {
        int sides = CollisionHandler.checkCloseCalls(barrier, barriers, getXPadding(), getYPadding());

        if (isStuck(sides)) {
            barrier.stopMoving();
        } else if (shouldTurnBack(sides)) {
            barrier.turnBack();
        }
    }

    /**
     * Checks if the barrier is stuck considering its current direction, current angle and the sides that is close to a barrier.
     * Sides is a 4-bit integer where the bits represent the sides of the barrier in the order of left, bottom, right, top.
     *
     * @param sides 4 bit `lbrt` number, representing close calls from each side
     * @return whether the barrier can move or not
     */
    private boolean isStuck(int sides) {
        final var PI = Math.PI;

        if (0.375 * PI <= angle && angle < 0.625 * PI) { // top
            return (sides & 0b1010) == 0b1010; // cannot move l or r
        } else if (0.875 * PI <= angle && angle < 1.125 * PI) { // left
            return (sides & 0b0101) == 0b0101; // cannot move b or t
        } else if (1.375 * PI <= angle && angle < 1.625 * PI) { // bottom
            return (sides & 0b1010) == 0b1010; // cannot move l or r
        } else if (1.875 * PI <= angle || angle < 0.125 * PI) { // right
            return (sides & 0b0101) == 0b0101; // cannot move t or b
        } else {
            logger.debug("Sides from the CollisionHandler: " + Integer.toBinaryString(sides));
            return (sides & 0b1010) == 0b1010 || (sides & 0b0101) == 0b0101;
        }
    }

    private boolean shouldTurnBack(int sides) {
        return !(direction == Direction.STIFF) && (direction == Direction.CLOCKWISE && !canGoClockwise(sides)) || (direction == Direction.COUNTER_CLOCKWISE && !canGoCounterClockwise(sides));
    }

    private boolean canGoClockwise(int sides) { // sides = `lbrt`
        final var PI = Math.PI;

        if (0.375 * PI <= angle && angle < 0.625 * PI) { // top
            return (sides & 0b0010) == 0; // right
        } else if (0.875 * PI <= angle && angle < 1.125 * PI) { // left
            return (sides & 0b0001) == 0; // top
        } else if (1.375 * PI <= angle && angle < 1.625 * PI) { // bottom
            return (sides & 0b1000) == 0; // left
        } else if (1.875 * PI <= angle || angle < 0.125 * PI) { // right
            return (sides & 0b0100) == 0; // bottom
        } else if (0.125 * PI <= angle && angle < 0.375 * PI) { // top-right
            return (sides & 0b0010) == 0 && (sides & 0b0100) == 0; // bottom and right
        } else if (0.625 * PI <= angle && angle < 0.875 * PI) { // top-left
            return (sides & 0b0010) == 0 && (sides & 0b0001) == 0; // right and top
        } else if (1.125 * PI <= angle && angle < 1.375 * PI) { // bottom-left
            return (sides & 0b0001) == 0 && (sides & 0b1000) == 0; // top and left
        } else { // bottom-right
            return (sides & 0b0100) == 0 && (sides & 0b1000) == 0; // left and bottom
        }
    }

    private boolean canGoCounterClockwise(int sides) { // sides = `lbrt
        final var PI = Math.PI;

        if (0.375 * PI <= angle && angle < 0.625 * PI) { // top
            return (sides & 0b1000) == 0; // left
        } else if (0.875 * PI <= angle && angle < 1.125 * PI) { // left
            return (sides & 0b0100) == 0; // bottom
        } else if (1.375 * PI <= angle && angle < 1.625 * PI) { // bottom
            return (sides & 0b0010) == 0; // right
        } else if (1.875 * PI <= angle || angle < 0.125 * PI) { // right
            return (sides & 0b0001) == 0; // top
        } else if (0.125 * PI <= angle && angle < 0.375 * PI) { // top-right
            return (sides & 0b0001) == 0 && (sides & 0b1000) == 0; // top and left
        } else if (0.625 * PI <= angle && angle < 0.875 * PI) { // top-left
            return (sides & 0b0100) == 0 && (sides & 0b1000) == 0; // bottom and left
        } else if (1.125 * PI <= angle && angle < 1.375 * PI) { // bottom-left
            return (sides & 0b0100) == 0 && (sides & 0b0010) == 0; // bottom and right
        } else { // bottom-right
            return (sides & 0b0001) == 0 && (sides & 0b0010) == 0; // top and right
        }
    }

    @Override
    public void adjustMovementParameters(Barrier barrier) {
        radius = LanceOfDestiny.getScreenWidth() * 0.15;
        centerX = barrier.getXPosition() + barrier.getLength() / 2 - radius * Math.cos(angle);
        centerY = barrier.getYPosition() + barrier.getThickness() / 2 + radius * Math.sin(angle);
    }

    private enum Direction {
        CLOCKWISE, STIFF, COUNTER_CLOCKWISE
    }
}
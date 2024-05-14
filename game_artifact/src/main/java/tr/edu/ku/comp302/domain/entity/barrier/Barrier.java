package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.IMovementStrategy;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.geom.Rectangle2D;
import java.security.SecureRandom;
import java.util.List;

public abstract class Barrier extends Entity {
    protected static final double DEFAULT_THICKNESS = 20;
    protected int health;
    protected final SecureRandom random;
    protected IMovementStrategy movementStrategy;
    private double length;
    private double thickness;
    private long lastDiceRollTimeNs;

    public Barrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        thickness = DEFAULT_THICKNESS;
        health = 1;
        length = LanceOfDestiny.getScreenWidth() / 50.;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        random = new SecureRandom();
        lastDiceRollTimeNs = 0;
    }

    public void adjustPositionAndSize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
        updatePositionRelativeToScreen(oldWidth, oldHeight, newWidth, newHeight);
        setLength(newWidth / 50.); // changes other size-relative instances too.
        movementStrategy.adjustMovementParameters(this);
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void decreaseHealth() {
        health--;
    }

    public double getSpeed() {
        return LanceOfDestiny.getScreenWidth() / 40.0; // random value, but it's not in the pdf so whatever
    }

    public void move(double speed) {
        movementStrategy.move(this, speed);
    }

    public void turnBack() {
        movementStrategy.turnBack();
    }

    public void stopMoving() {
        movementStrategy.stopMoving();
    }

    public int getXDirection() {
        return movementStrategy.getXDirection();
    }

    public int getYDirection() {
        return movementStrategy.getYDirection();
    }

    public IMovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public long getLastDiceRollTime() {
        return lastDiceRollTimeNs;
    }

    public void tryMove(long currentTime) {
        lastDiceRollTimeNs = currentTime;
        boolean willMove = random.nextDouble() <= 0.2;
        if (getXDirection() == 0 && getYDirection() == 0 && willMove) {
            movementStrategy.startMoving();
        }
    }

    public void tryStop(long currentTime) {
        lastDiceRollTimeNs = currentTime;
        boolean willStop = random.nextDouble() <= 0.2;
        if (isMoving() && willStop) {
            stopMoving();
        }
    }

    public boolean isMoving() {
        return getXDirection() != 0 || getYDirection() != 0;
    }

    public void handleCloseCalls(List<Barrier> barriers) {
        if (movementStrategy != null) {
            movementStrategy.handleCloseCalls(this, barriers);
        }
    }
}

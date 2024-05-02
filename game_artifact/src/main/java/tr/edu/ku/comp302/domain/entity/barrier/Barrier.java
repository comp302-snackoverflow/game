package tr.edu.ku.comp302.domain.entity.barrier;

import java.awt.geom.Rectangle2D;
import java.util.List;

import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.IMovementStrategy;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public abstract class Barrier extends Entity {
    protected static final double DEFAULT_THICKNESS = 20;
    protected int health;
    protected double length;
    protected double thickness;
    protected double speed;
    IMovementStrategy movementStrategy;

    public Barrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        thickness = DEFAULT_THICKNESS;
        health = 1;
        length = LanceOfDestiny.getScreenWidth() / 50.;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
        speed = 0;      // By default.
    }

    public void adjustPositionAndSize(int oldWidth, int oldHeight, int newWidth, int newHeight){
        updatePositionRelativeToScreen(oldWidth, oldHeight, newWidth, newHeight);
        setLength(newWidth / 50.);      // changes other size-relative instances too.
    }

    public void setLength(double length) {
        this.length = length;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
    }
    @Override
    public void handleCollision(boolean isWall) {
        health--;
        // FIXME: What is this ? CollisionBehavior.handleCollision(this);
    }

    public boolean isDead() {
        return health <= 0;
    }

    public double getLength() {
        return length;
    }
    public double getThickness() {
        return thickness;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    
    public void checkCollision(List<Barrier> barriers){
        movementStrategy.checkCollision(barriers);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void move() {
        movementStrategy.move();
    }

    public IMovementStrategy getMovementStrategy() {
        return movementStrategy;
    }

    public void setMovementStrategy(IMovementStrategy movementStrategy) {
        this.movementStrategy = movementStrategy;
    }
}

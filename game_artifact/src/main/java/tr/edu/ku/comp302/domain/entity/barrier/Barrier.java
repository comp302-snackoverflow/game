package tr.edu.ku.comp302.domain.entity.barrier;

import java.awt.geom.Rectangle2D;
import java.util.List;

import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.IMovementStrategy;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.view.BarrierView;

public abstract class Barrier extends Entity {
    protected static final double DEFAULT_THICKNESS = 20;
    protected static final int DEFAULT_HEALTH = 1;
    protected static final double DEFAULT_SPEED = 0;    // TODO: Change this and calculate proper speed.
    protected int health;
    protected double length;
    protected double thickness;
    protected double speed;
    protected double L;
    IMovementStrategy MovementStrategy;

    public Barrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        thickness = DEFAULT_THICKNESS;
        speed = DEFAULT_SPEED;
        health = DEFAULT_HEALTH;
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
        length = LanceOfDestiny.getScreenWidth() / 50.;
    }

    public void setL(double l) {
        L = l;
        length = L / (double) 5;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
    }

    public void adjustPositionAndSize(int oldWidth, int oldHeight, int newWidth, int newHeight){
        updatePositionRelativeToScreen(oldWidth, oldHeight, newWidth, newHeight);
        setLength(newWidth / 10.);      // changes other size-relative instances too.
    }

    public void setLength(double length) {
        this.length = length;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
    }
    @Override
    public void handleCollision(boolean isWall) {
        health--;
        //CollisionBehavior.handleCollision(this);
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

    
    public void checkCollision(List<BarrierView> barrierViews){
        MovementStrategy.checkCollision(barrierViews);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public void move() {
        MovementStrategy.move();
    }

    public IMovementStrategy getMovementStrategy() {
        return MovementStrategy;
    }

    public void setMovementStrategy(IMovementStrategy movementStrategy) {
        MovementStrategy = movementStrategy;
    }
}

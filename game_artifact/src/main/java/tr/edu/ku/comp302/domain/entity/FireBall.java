package tr.edu.ku.comp302.domain.entity;


import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.security.SecureRandom;

public class FireBall extends Entity {
    private boolean isOverwhelmed = false;
    private int size = 32;
    private double dx = 0;
    private double dy = 0;
    private double speed = 2; // Might change the speed later.
    //TODO: Add the player!

    public FireBall(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
        actualShape = new Ellipse2D.Double(xPosition, yPosition, size, size);

    }

    @Override
    public void handleCollision(boolean isWall) {
        if (isWall) {
            dx = -dx;
            return;
        }
        dy = -dy; // TODO calculate new speed here;
        dx = new SecureRandom().nextInt(-3, 4);
    }

    public void move() {
        xPosition += dx;
        yPosition += dy;
        boundingBox.setRect(xPosition, yPosition, size, size);
        actualShape.setFrame(xPosition, yPosition, size, size);
    }
    public void launchFireball() {
        this.dy = speed;
    }

    public void increaseSpeed(double updateVal) {
        this.speed += updateVal;
    }

    public void bounceOffVerticalSurface() {
        this.dx = -this.dx;
    }

    public void bounceOffHorizontalSurface() {
        this.dy = -this.dy;
    }

    //TODO: These are the tentative bouncing mechanisms, and they DO NOT WORK! do not trust these methods.

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setOverwhelmed(boolean isOverwhelmed) {
        this.isOverwhelmed = isOverwhelmed;
    }

    public boolean getOverwhelmed () {
        return isOverwhelmed;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    //TODO: Make a hitbarrier method !


    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}


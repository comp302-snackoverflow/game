package tr.edu.ku.comp302.domain.entity;


import tr.edu.ku.comp302.domain.handler.collision.Collision;

import javax.swing.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.security.SecureRandom;
import java.lang.Math.*;

public class FireBall extends Entity {
    private boolean isOverwhelmed = false;
    private int size = 16;
    private double dx = 0;
    private double dy = 0;
    private double speed = 2; // Might change the speed later.
    //TODO: Add the player!
    private boolean moving;

    public FireBall(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
        actualShape = new Ellipse2D.Double(xPosition, yPosition, size, size);
        moving = false;
    }

    @Override
    public void handleCollision(boolean isWall) {
        if (isWall) {
            dx = -dx;
            return;
        }
        dy = -dy; // TODO calculate new speed here;
        dx = -dx;
        //dx = new SecureRandom().nextInt(-3, 4);
    }

    // for handling reflections with steady surfaces
    // need to pass the surface angle
    public void handleReflection(double surfaceAngleDegrees) {
        double surfaceAngle = Math.toRadians(surfaceAngleDegrees);
        double totalSpeedAngle = Math.atan2(dy, dx);

        double newAngle = 2 * surfaceAngle - totalSpeedAngle;

        double totalSpeed = Math.sqrt(dx * dx + dy * dy);
        dx = totalSpeed * Math.cos(newAngle);
        dy = totalSpeed * Math.sin(newAngle);
    }
    // for handling reflections with moving surfaces
    // need to pass the surface angle and the surface speed
    // works for steady surfaces as well
    public void handleReflection(double surfaceAngleDegrees, double surfaceXSpeed) {
        double surfaceAngleRadians = Math.toRadians(surfaceAngleDegrees);
        double totalSpeedAngle = Math.atan2(dy, dx); //FireBall speed angle
        if (surfaceXSpeed != 0) { // if the lance is moving
            if (dx == 0 && surfaceAngleDegrees == 0) { // perpendicular collision
                double dir = Math.signum(surfaceXSpeed);
                dx = dir * dy / Math.sqrt(2);
                dy = -dy * Math.sqrt(2);
            }
            else if (Math.signum(surfaceXSpeed) == Math.signum(dx)) { // in the same direction
                double currentSpeed = Math.sqrt(dx * dx + dy * dy);
                //double newSpeed = currentSpeed + 5; // increase total speed by 5
                double newSpeed = currentSpeed; // because permanent +5 speed boost looks bad
                dx = newSpeed * Math.cos(totalSpeedAngle);
                dy = -newSpeed * Math.sin(totalSpeedAngle);
            } else { // in the opposite direction
                // mirror the movement of the FireBall
                dx = -dx;
                dy = -dy;
            }
        } else { // lance is not moving
            // calculate reflection normally
            double newAngle = 2 * surfaceAngleRadians - totalSpeedAngle;
            double newSpeed = Math.sqrt(dx * dx + dy * dy);
            dx = newSpeed * Math.cos(newAngle);
            dy = newSpeed * Math.sin(newAngle);
        }
    }

    public void handleCornerReflection(double surfaceAngleDegrees,  Collision corner) {
        switch (corner) {
            case TOP_RIGHT, BOTTOM_LEFT:
                handleReflection(surfaceAngleDegrees + 45);
                break;
            case TOP_LEFT, BOTTOM_RIGHT:
                handleReflection(surfaceAngleDegrees - 45);
                break;
            default:
                handleReflection(surfaceAngleDegrees);
                break;
        }
    }

    // for handling reflections at a surface's corner
    // need to pass which corner the collision happened
    public void handleCornerReflection(double surfaceAngleDegrees, double surfaceXSpeed, Collision corner) {
        switch (corner) {
            case TOP_RIGHT, BOTTOM_LEFT:
                handleReflection(surfaceAngleDegrees + 45, surfaceXSpeed);
                break;
            case TOP_LEFT, BOTTOM_RIGHT:
                handleReflection(surfaceAngleDegrees - 45, surfaceXSpeed);
                break;
            default:
                handleReflection(surfaceAngleDegrees, surfaceXSpeed);
                break;
        }
    }

    public void move() {
        xPosition += dx;
        yPosition += dy;
        boundingBox.setRect(xPosition, yPosition, size, size);
        actualShape.setFrame(xPosition, yPosition, size, size);
    }

    public void stickToLance(Lance lance) {
        this.xPosition = lance.getXPosition() + lance.getLength() / 2 - (int) (size / 2.0);
        this.yPosition = lance.getYPosition() - size;
        this.boundingBox.setRect(xPosition, yPosition, size, size);
        this.actualShape.setFrame(xPosition, yPosition, size, size);
    }

    public void launchFireball() {
        moving = true;
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

    public boolean isMoving() {
        return moving;
    }
}

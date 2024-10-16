package tr.edu.ku.comp302.domain.entity;

import tr.edu.ku.comp302.domain.handler.collision.Collision;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.geom.Rectangle2D;

public class FireBall extends Entity {
    private int size = 16;
    private double dx = 0;
    private double dy = 0;
    private double speed; // in px/s
    private boolean isOverwhelming = false;

    public FireBall(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        speed = 0.2 * LanceOfDestiny.getScreenWidth();
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }

    public void adjustPositionAndSpeed(double oldWidth, double oldHeight, double newWidth, double newHeight) {
        updatePositionRelativeToScreen(oldWidth, oldHeight, newWidth, newHeight);
        speed = speed * newWidth / oldWidth;
        dx = dx * newWidth / oldWidth;
        dy = dy * newHeight / oldHeight;
    }

    // for handling reflections with moving surfaces
    // need to pass the surface angle and the surface speed
    // works for steady surfaces as well
    public void handleReflection(double surfaceAngleDegrees, double surfaceXSpeed) {
        /*
        double DX,myDX,DY,myDY;
        System.out.print("\nBEFORE REFLECTION: ");
        DX = dx * 1000;
        myDX = Math.round(DX);
        myDX = myDX / 1000;
        DY = dy * 1000;
        myDY = Math.round(DY);
        myDY = myDY / 1000;
        System.out.println(myDX+ " " + myDY);
        */

        double surfaceAngleRadians = Math.toRadians(surfaceAngleDegrees);
        double totalSpeedAngle = Math.atan2(-dy, dx); //FireBall speed angle
        double newAngle = 2 * surfaceAngleRadians - totalSpeedAngle;

        double totalSpeed = Math.sqrt(dx * dx + dy * dy);

        //System.out.println("\n\n\nOLD ANGLE: " + 360*totalSpeedAngle/(2*Math.PI));
        //System.out.println("SURFACE ANGLE: " + surfaceAngleDegrees);

        if (surfaceXSpeed != 0) { // if the lance is moving
            if (Math.abs(dx) < 0.001 && Math.abs(surfaceAngleDegrees) < 0.001) { // perpendicular collision
                double dir = Math.signum(surfaceXSpeed);
                dx = dir * dy / Math.sqrt(2);
                dy = -dy / Math.sqrt(2);
            } else if (Math.signum(surfaceXSpeed) == Math.signum(dx)) { // in the same direction
                //double newSpeed = totalSpeed + 5; // increase total speed by 5
                double newSpeed = totalSpeed; // because permanent +5 speed boost looks bad
                dx = newSpeed * Math.cos(newAngle);
                dy = newSpeed * -Math.sin(newAngle);
            } else { // in the opposite direction
                // mirror the movement of the FireBall
                dx = -dx;
                dy = -dy;
            }
        } else { // lance is not moving
            // calculate reflection normally
            dx = totalSpeed * Math.cos(newAngle);
            dy = totalSpeed * -Math.sin(newAngle);

            //System.out.println("NEW ANGLE " + 360*newAngle/(2*Math.PI));
        }

        /*
        System.out.print("AFTER REFLECTION: ");
        DX = dx * 1000;
        myDX = Math.round(DX);
        myDX = myDX / 1000;
        DY = dy * 1000;
        myDY = Math.round(DY);
        myDY = myDY / 1000;
        System.out.println(myDX+ " " + myDY);
        */
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

    public void move(double dx, double dy) {
        xPosition += dx;
        yPosition += dy;
        boundingBox.setRect(xPosition, yPosition, size, size);
    }

    public void stickToLance(Lance lance) {
        this.xPosition = lance.getXPosition() + lance.getLength() / 2 - (int) (size / 2.0);
        this.yPosition = lance.getYPosition() - size - 2; // 2 more pxs to prevent fireball from getting stuck
        this.boundingBox.setRect(xPosition, yPosition, size, size);
    }

    public void launchFireball() {
        this.dy = speed;
    }

    public void stopFireball() {
        this.dx = 0;
        this.dy = 0;
    }

    public void bounceOffVerticalSurface() {
        this.dx = -this.dx;
    }

    public void bounceOffHorizontalSurface() {
        this.dy = -this.dy;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isMoving() {
        return dx != 0 || dy != 0;
    }

    public boolean isOverwhelming() {
        return isOverwhelming;
    }

    public void setOverwhelming(boolean isOverwhelming) {
        this.isOverwhelming = isOverwhelming;
    }

    public void applyDoubleAccel() {
        this.speed /= 2;
        dx /= 2;
        dy /= 2;
    }

    public void revertDoubleAccel() {
        this.speed *= 2;
        dx *= 2;
        dy *= 2;
    }
}

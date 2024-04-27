package tr.edu.ku.comp302.domain.entity;

import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.geom.Rectangle2D;

public class Lance extends Entity {
    private double length;
    private double speedWithHold;
    private double speedWithTap;
    private double thickness;
    private short direction;
    private double rotationAngle;
    public static final double rotationSpeed = 20.0;
    public static final double horizontalRecoverySpeed = 45.0;

    public Lance(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
        direction = 0;
        length = LanceOfDestiny.getScreenWidth() / 10.;
        speedWithHold = 2 * length;
        speedWithTap = length;
        thickness = 20;
    }

    public void incrementRotationAngle(double degrees) {
        if (canRotateClockwise(degrees) && canRotateCounterClockwise(degrees)) {
            rotationAngle += degrees;
        } else if (rotationAngle + degrees < -45) {
            rotationAngle = -45;
        } else if (rotationAngle + degrees > 45) {
            rotationAngle = 45;
        }
    }

    public void returnToHorizontalState(double degrees) {
        if (degrees >= Math.abs(rotationAngle)) {
            rotationAngle = 0;
        } else if (rotationAngle > 0) {
            incrementRotationAngle(-degrees);
        } else if (rotationAngle < 0) {
            incrementRotationAngle(degrees);
        }
    }
    // TODO: set bounding box in these methods
    public void adjustPositionAndSize(int oldWidth, int oldHeight, int newWidth, int newHeight){
        updatePositionRelativeToScreen(oldWidth, oldHeight, newWidth, newHeight);
        setLength(newWidth / 10.);      // changes other size-relative instances too.
    }

    public boolean canRotateCounterClockwise(double degrees) {
        return rotationAngle + degrees >= -45;
    }

    public boolean canRotateClockwise(double degrees) {
        return rotationAngle + degrees <= 45;
    }

    public void updateXPosition(int dx) {
        xPosition += dx * direction;
    }

    @Deprecated(forRemoval = true) // Lance doesn't move vertically, right?
    public void updateYPosition(int updateVal) {
        yPosition += updateVal;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
        speedWithHold = length * 2;
        speedWithTap = length;
        // TODO: update bounding box and actual hitbox here
    }

    public double getThickness() {
        return thickness;
    }

    public void setThickness(double thickness) {
        this.thickness = thickness;
    }

    public double getSpeedWithHold() {
        return speedWithHold;
    }

    public void setSpeedWithHold(double speedWithHold) {
        this.speedWithHold = speedWithHold;
    }

    public double getSpeedWithTap() {
        return speedWithTap;
    }

    public void setSpeedWithTap(double speedWithTap) {
        this.speedWithTap = speedWithTap;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    // TODO: Implement collision for Lance
    public void handleCollision(boolean isWall) {
        // nothing happens
    }

    public void setDirection(int direction) {
        if (direction > 1 || direction < -1) {
            throw new IllegalArgumentException("Direction can be -1 for left, 0 for no movement, 1 for right");
        }
        this.direction = (short) direction;
    }
}

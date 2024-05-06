package tr.edu.ku.comp302.domain.entity;

import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Lance extends Entity {
    public static final double rotationSpeed = 20.0;
    public static final double horizontalRecoverySpeed = 45.0;
    private double length;
    private double speedWithHold;
    private double speedWithTap;
    private double thickness;
    private short direction;
    private double rotationAngle;

    public Lance(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
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
    public void adjustPositionAndSize(int oldWidth, int oldHeight, int newWidth, int newHeight) {
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
        double newPos = xPosition + dx * direction;
        if (newPos < 0 || newPos + length > LanceOfDestiny.getScreenWidth()) {
            return;
        }
        xPosition = newPos;
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
        boundingBox.setRect(xPosition, yPosition, length, thickness);
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

    public void setDirection(int direction) {
        if (direction > 1 || direction < -1) {
            throw new IllegalArgumentException("Direction can be -1 for left, 0 for no movement, 1 for right");
        }
        this.direction = (short) direction;
    }

    // TODO: FINISH THIS METHOD
    public Polygon getActualHitbox() {
        double B = Math.toRadians(rotationAngle);
        double x = getXPosition();
        double y = getYPosition() + thickness;

        // m is the length of the bounding box
        // n is the width of the bounding box
        double m = length * Math.cos(B) + thickness * Math.abs(Math.sin(B));
        double n = length * Math.abs(Math.sin(B)) + thickness * Math.cos(B);

        // top left corner point of the bounding box
        double boundingX = x - ((m - length) / 2);
        double boundingY = y - ((n + thickness) / 2);

        // x1,y1 top left
        // x2,y2 top right
        // x3,y3 bottom right
        // x4, y4 bottom left
        double x1, y1, x2, y2, x3, y3, x4, y4;

        if (Math.sin(B) < 0) {
            x1 = boundingX;
            y1 = boundingY + length * Math.abs(Math.sin(B));
            x2 = boundingX + length * Math.cos(B);
            y2 = boundingY;
            x3 = boundingX + m;
            y3 = boundingY + thickness * Math.cos(B);
            x4 = boundingX + thickness * Math.abs(Math.sin(B));
            y4 = boundingY + n;
        } else {
            x1 = boundingX + thickness * Math.abs(Math.sin(B));
            y1 = boundingY;
            x2 = boundingX + m;
            y2 = boundingY + length * Math.abs(Math.sin(B));
            x3 = boundingX + length * Math.cos(B);
            y3 = boundingY + n;
            x4 = boundingX;
            y4 = boundingY + thickness * Math.cos(B);
        }

        int[] xPoints = {(int) x1, (int) x2, (int) x3, (int) x4};
        int[] yPoints = {(int) y1, (int) y2, (int) y3, (int) y4};

        return new Polygon(xPoints, yPoints, 4);
    }

    @Override
    public Rectangle2D getBoundingBox() {
        return getActualHitbox().getBounds2D();
    }
}

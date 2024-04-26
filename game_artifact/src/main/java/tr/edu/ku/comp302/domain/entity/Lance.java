package tr.edu.ku.comp302.domain.entity;

public class Lance extends Entity {
    private double length;
    private double L;

    private double thickness = 20;

    private double speedWithHold;
    private double speedWithTap;
    private short direction;

    private double rotationAngle;
    public static final double rotationSpeed = 20.0;
    public static final double horizontalRecoverySpeed = 45.0;

    public Lance(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        direction = 0;
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
    }

    public double getL() {
        return L;
    }

    public void setL(double l) {
        L = l;
        updateRelativeToL();
    }

    // I think this should be moved inside setL
    private void updateRelativeToL() {
        setLength(L);
        setThickness(20);
        setSpeedWithTap(L);
        setSpeedWithHold(2 * L);
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
}

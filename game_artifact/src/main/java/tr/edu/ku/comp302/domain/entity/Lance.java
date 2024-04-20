package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

public class Lance extends Entity{

    private double length;
    private double L;

    private double thickness = 20;

    private double speedWithHold;
    private double speedWithTap;

    private double rotationAngle;
    public static final double rotationSpeed = 20.0;
    public static final double horizontalRecoverySpeed = 45.0;

    public Lance(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
        System.out.println(xPosition);
    }

    public void incrementRotationAngle(double degrees){
        if (canRotateClockwise(degrees) && canRotateCounterClockwise(degrees)){
            rotationAngle += degrees;
        }else if (rotationAngle + degrees < -45){
            rotationAngle = -45;
        }else if (rotationAngle + degrees > 45){
            rotationAngle = 45;
        }
    }

    public void stayInSteadyState(double degrees){
        if (degrees >= Math.abs(rotationAngle)) {
            rotationAngle = 0;
        } else if (rotationAngle > 0) {       // can also use incrementRotationAngle(Math.signum(rotationAngle) * -degrees);
            incrementRotationAngle(-degrees); // instead of another branch; however, this is probably more readable
        } else if (rotationAngle < 0) {
            incrementRotationAngle(degrees);
        }

    }
    public boolean canRotateCounterClockwise(double degrees){
        return rotationAngle + degrees >= -45;
    }
    public boolean canRotateClockwise(double degrees){
        return rotationAngle + degrees <= 45;
    }



    public void updateXPosition(int updateVal){
        xPosition += updateVal;
        System.out.println(xPosition);
    }

    public void updateYPosition(int updateVal){
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

    private void updateRelativeToL(){
        setLength(L);
        setThickness(20);
        boundingBox.setRect(xPosition, yPosition, length, thickness);
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

    // TODO: Implement collision for Lance
    public void handleCollision(boolean isWall) {
        // nothing happens
    }
}

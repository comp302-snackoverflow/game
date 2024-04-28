package tr.edu.ku.comp302.domain.entity;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.lang.Math.*;

public class Lance extends Entity{

    private double length;
    private double L;

    private double thickness = 20;

    private double W = 20;

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
        //boundingBox.setRect(xPosition, yPosition, length, thickness);
        boundingBox = getLanceBounds();
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

    public short getDirection() {
        return direction;
    }

    // TODO: FINISH THIS METHOD
    public Polygon getActualHitbox() {
        double B = Math.toRadians(rotationAngle);
        double x = getXPosition();
        double y = getYPosition() + W;

        // m is the length of the bounding box
        // n is the width of the bounding box
        double m = L * Math.cos(B) + W * Math.abs(Math.sin(B));
        double n = L * Math.abs(Math.sin(B)) + W * Math.cos(B);

        // top left corner point of the bounding box
        double boundingX = x - ((m - L) / 2);
        double boundingY = y - ((n + W) / 2);

        // x1,y1 top left
        // x2,y2 top right
        // x3,y3 bottom right
        // x4, y4 bottom left
        double x1, y1, x2, y2, x3, y3, x4, y4;

        if (Math.sin(B) < 0) {
            x1 = boundingX;
            y1 = boundingY + L * Math.abs(Math.sin(B));
            x2 = boundingX + L * Math.cos(B);
            y2 = boundingY;
            x3 = boundingX + m;
            y3 = boundingY + W * Math.cos(B);
            x4 = boundingX + W * Math.abs(Math.sin(B));
            y4 = boundingY + n;
        }
        else {
            x1 = boundingX + W * Math.abs(Math.sin(B));
            y1 = boundingY;
            x2 = boundingX + m;
            y2 = boundingY + L * Math.abs(Math.sin(B));
            x3 = boundingX + L * Math.cos(B);
            y3 = boundingY + n;
            x4 = boundingX;
            y4 = boundingY + W * Math.cos(B);
        }

        int[] xPoints = {(int) x1, (int) x2, (int) x3, (int) x4};
        int[] yPoints = {(int) y1, (int) y2, (int) y3, (int) y4};

        return new Polygon(xPoints, yPoints, 4);
    }

    public Rectangle getLanceBounds() {
        return getActualHitbox().getBounds();
    }
}

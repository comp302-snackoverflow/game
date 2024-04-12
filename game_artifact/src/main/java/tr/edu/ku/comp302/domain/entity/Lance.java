package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;
public class Lance extends Entity{

    private double length;
    private double L;
    private double thickness = 20;
    private double speedWithHold;
    private double speedWithTap;

    // TODO: add rotationAngle later on

    public Lance(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
    }

    public void updateXPosition(int updateVal){
        if (xPosition + updateVal < 0 || xPosition + updateVal + length > screenWidth) {
            return;
        }
        xPosition += updateVal;
        boundingBox.setRect(xPosition, yPosition, length, thickness);
        actualShape = boundingBox;
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

    public void handleCollision(boolean isWall) {
        // nothing happens
    }
}

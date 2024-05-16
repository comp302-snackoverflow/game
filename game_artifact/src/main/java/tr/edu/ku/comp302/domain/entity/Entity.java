package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
    protected double xPosition;
    protected double yPosition;
    protected Rectangle2D boundingBox;

    // TODO: screen size something
    public Entity(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void updatePositionRelativeToScreen(double oldWidth, double oldHeight, double newWidth, double newHeight) {
        xPosition = xPosition * newWidth / oldWidth;
        yPosition = yPosition * newHeight / oldHeight;
        boundingBox.setRect(xPosition, yPosition, boundingBox.getWidth(), boundingBox.getHeight());
    }

    public double getXPosition() {
        return xPosition;
    }

    public void setXPosition(double xPosition) {
        this.xPosition = xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }

    public void setYPosition(double yPosition) {
        this.yPosition = yPosition;
    }

    public Rectangle2D getBoundingBox() {
        return boundingBox;
    }

}


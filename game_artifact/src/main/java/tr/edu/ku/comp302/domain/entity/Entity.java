package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

public abstract class Entity {
    protected double xPosition;
    protected double yPosition;
    protected Rectangle2D boundingBox;
    protected RectangularShape actualShape;
    protected double screenWidth;
    protected double screenHeight;

    public Entity(double xPosition, double yPosition, double screenWidth, double screenHeight){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
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

    public RectangularShape getActualShape() {
        return actualShape;
    }

    public abstract void handleCollision(boolean isWall); //entity,integer input ??
}

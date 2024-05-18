package tr.edu.ku.comp302.domain.entity;

import java.awt.geom.Rectangle2D;

public abstract class Entity {
    // OVERVIEW: This class represents a general game entity, which has a position, and a bounding box
    // to check for collisions. It provides some setter and getter methods, and also provides a method
    // to update an entity's position as the user resizes the screen.

    // ABSTRACTION FUNCTION:
    // A typical entity is going to be in the form of (x, y) + boundingBox, where x is the x coordinate
    // of the entity, and y is the y coordinate of the entity. Its abstraction function can be described as:
    // AF(c) = (c.xPosition, c.yPosition) & c.boundingBox.

    // REP INVARIANT:
    // The representation invariant of an Entity class should be as follows:
    // c.xPosition >= 0 and c.yPosition >= 0
    // c.boundingBox should not be null.
    // c.boundingBox.getX() = c.xPosition
    // c.boundingBox.getY() = c.yPosition
    // In brief, the x and y coordinates cannot be negative, the bounding box should not be null,
    // and if the position of the Entity is updated, the bounding box must be updated accordingly at all times.

    protected double xPosition;
    protected double yPosition;
    protected Rectangle2D boundingBox;

    // TODO: screen size something
    public Entity(double xPosition, double yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
    }

    public void updatePositionRelativeToScreen(int oldWidth, int oldHeight, int newWidth, int newHeight) {
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

    public boolean repOk() {
        // EFFECTS: Returns true if the representation invariant of the Entity class holds true,
        // otherwise returns false.
        if (this.xPosition < 0 || this.yPosition < 0) return false;
        if (this.boundingBox == null) return false;
        if (this.boundingBox.getX() != this.xPosition || this.boundingBox.getY() != this.yPosition) return false;

        return true;
    }

}


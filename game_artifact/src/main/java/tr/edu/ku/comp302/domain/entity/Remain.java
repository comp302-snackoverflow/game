package tr.edu.ku.comp302.domain.entity;

import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.geom.Rectangle2D;

public class Remain extends Entity {
    private double speed = 0.2 * LanceOfDestiny.getScreenWidth(); // 2L, selected randomly. Changed so the unit is px/s
    private int size = 50;
    private boolean isDropped = false;

    public Remain(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void move(double dy) {
        yPosition += dy;
        boundingBox.setRect(xPosition, yPosition, size, size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isDropped() {
        return isDropped;
    }

    public void drop() {
        isDropped = true;
    }
}

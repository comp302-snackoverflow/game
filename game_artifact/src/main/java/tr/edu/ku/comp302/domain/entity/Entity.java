package tr.edu.ku.comp302.domain.entity;

public abstract class Entity {
    protected double xPosition;
    protected double yPosition;

    public Entity(double xPosition, double yPosition){
        this.xPosition = xPosition;
        this.yPosition = yPosition;
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
}

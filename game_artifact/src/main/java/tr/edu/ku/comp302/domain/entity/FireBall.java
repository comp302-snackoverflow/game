package tr.edu.ku.comp302.domain.entity;

public class FireBall extends Entity {
    private boolean isOverwhelmed = false;
    private int size = 16;
    private double dx = 0;
    private double dy = 0;
    private double speed = 2; // Might change the speed later.

    //TODO: Add the player!

    public FireBall(double xPosition, double yPosition) {
        super(xPosition, yPosition);
    }

    public void move() {
        xPosition += dx;
        yPosition += dy;
    }
    public void launchFireball() {
        this.dy = speed;
    }

    public void increaseSpeed(double updateVal) {
        this.speed += updateVal;
    }

    public void bounceOffVerticalSurface() {
        this.dx = -this.dx;
    }

    public void bounceOffHorizontalSurface() {
        this.dy = -this.dy;
    }

    //TODO: These are the tentative bouncing mechanisms, and they DO NOT WORK! do not trust these methods.

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setOverwhelmed(boolean isOverwhelmed) {
        this.isOverwhelmed = isOverwhelmed;
    }

    public boolean getOverwhelmed () {
        return isOverwhelmed;
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    //TODO: Make a hitbarrier method !


    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}


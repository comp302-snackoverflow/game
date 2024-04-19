package tr.edu.ku.comp302.domain.entity;

import tr.edu.ku.comp302.domain.entity.Barriers.ExplosiveBarrier;

public class Remains extends Entity{
    
    private double speed = 1.5;


    public Remains(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    //collision with lance to be added

    @Override
    public void handleCollision(boolean isWall) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleCollision'");
    }
    
    
}

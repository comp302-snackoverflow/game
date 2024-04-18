package tr.edu.ku.comp302.domain.entity.Barriers;

import tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies.CircularMovement;


public class ExplosiveBarrier extends Barrier {

    public ExplosiveBarrier(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        //TODO Auto-generated constructor stub
        ImagePath = "/assets/explosive_barrier.png";
        health = 1;
        this.MovementStrategy = new CircularMovement(screenWidth, screenHeight, this);
    }

}

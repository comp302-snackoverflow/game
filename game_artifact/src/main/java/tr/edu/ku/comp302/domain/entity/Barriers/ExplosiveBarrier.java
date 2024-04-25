package tr.edu.ku.comp302.domain.entity.Barriers;

import tr.edu.ku.comp302.domain.entity.Remains;
import tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies.CircularMovement;
import tr.edu.ku.comp302.ui.view.RemainsView;


public class ExplosiveBarrier extends Barrier {
    public static final String TYPE = "Explosive";
    public Remains remain;

    public ExplosiveBarrier(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        //TODO Auto-generated constructor stub
        ImagePath = "/assets/explosive_barrier.png";
        health = 1;
        this.MovementStrategy = new CircularMovement(screenWidth, screenHeight, this);
    }

    public Remains dropRemains(){
        // barrier is removed then the asset remains added
        // starting from the x and y position fot he center of
        // the barrier that was destroyed the remains fall with constant speed
        remain = new Remains(this.getXPosition() + this.getLength()/2, this.getYPosition() + this.getThickness()/2, screenWidth, screenHeight);
        //call to remain view here
        return remain;
    }
}

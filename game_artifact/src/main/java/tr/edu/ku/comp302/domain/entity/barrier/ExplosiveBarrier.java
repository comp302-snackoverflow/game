package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.CircularMovement;


public class ExplosiveBarrier extends Barrier {
    public static final String TYPE = "explosive";
    public Remain remain;

    public ExplosiveBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        health = 1;
        this.movementStrategy = new CircularMovement(this);
    }

    public Remain dropRemains(){
        // barrier is removed then the asset remains added 
        // starting from the x and y position fot he center of 
        // the barrier that was destroyed the remains fall with constant speed 
        remain = new Remain(getXPosition() + getLength() / 2, getYPosition() + getThickness() / 2);
        //call to remain view here
        return remain;
        
    }

}

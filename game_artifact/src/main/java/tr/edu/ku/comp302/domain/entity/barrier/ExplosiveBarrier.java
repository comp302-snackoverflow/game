package tr.edu.ku.comp302.domain.entity.barrier;

import tr.edu.ku.comp302.domain.entity.Remains;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.CircularMovement;


public class ExplosiveBarrier extends Barrier {
    public Remains remain;

    public ExplosiveBarrier(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        //TODO Auto-generated constructor stub
        ImagePath = "/assets/explosive_barrier.png";
        health = 1;
        this.MovementStrategy = new CircularMovement(this);
    }

    public Remains dropRemains(){
        // barrier is removed then the asset remains added 
        // starting from the x and y position fot he center of 
        // the barrier that was destroyed the remains fall with constant speed 
        remain = new Remains(this.getXPosition() + this.getLength()/2, this.getYPosition() + this.getThickness()/2);
        //call to remain view here
        return remain;
        
    }

}

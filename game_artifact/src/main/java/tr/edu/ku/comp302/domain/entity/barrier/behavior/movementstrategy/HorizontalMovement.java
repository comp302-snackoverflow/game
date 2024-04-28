package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;
import tr.edu.ku.comp302.ui.view.BarrierView;


/**
 * This strategy class calculates the movements and collisions of the horizontal moving barriers
 */
public class HorizontalMovement implements IMovementStrategy{
    private Barrier barrier;
    double barrierThickness;
    double barrierLength;
    boolean stiffness = true;
    long previousTime = 0;
    long currentTime = 0;
    static final short RIGHT_DIRECTION = 1;
    static final short LEFT_DIRECTION = 0;
    short direction = RIGHT_DIRECTION;
    double probabilisticMove = 0;

    static final double BARRIER_SPEED = 0.5;

    /**
     * Initialize essential instances of this class
     *
     * @param barrier the barrier itself
     */
    public HorizontalMovement(Barrier barrier){
        this.barrier = barrier;

        barrierLength = barrier.getLength();
        barrierThickness = barrier.getThickness();
    }


    /**
     * If the barrier is not stiff and the determined direction for movement is left,
     * moves with a speed of L/4 to the left, otherwise, the same logic applies to the right.
     */
    @Override
    public void move(){
        currentTime = System.currentTimeMillis();
        if (currentTime - previousTime >= 1000){
            probabilisticMove = Math.random();
            previousTime = currentTime;

        }
        //System.out.println(probabilisticMove);
        if(probabilisticMove <= 0.2 && probabilisticMove >= 0 ){
            if (!stiffness){
                barrier.setSpeed(BARRIER_SPEED);
    
                if (direction == RIGHT_DIRECTION) {
                    barrier.setXPosition(barrier.getXPosition() + barrier.getSpeed());
                    barrier.getBoundingBox().setRect(barrier.getXPosition() + barrier.getSpeed(), barrier.getYPosition(), barrier.getLength(), barrier.getThickness());
                }
    
                else {
                    barrier.setXPosition(barrier.getXPosition() - barrier.getSpeed());
                    barrier.getBoundingBox().setRect(barrier.getXPosition() - barrier.getSpeed(), barrier.getYPosition(), barrier.getLength(), barrier.getThickness());
                }
    
            }

        }

        
    }



   

    /**
     * This funciton takes the array of positions x and y a corner and checks if its intercepting other barrier area
     *
     *  */
    boolean calculateInterception(double[] cornerPoint, Barrier barrier, boolean isWest ){
        double screenWidth = LanceOfDestiny.getScreenWidth();

        double xBarrier = barrier.getXPosition();
        double yBarrier = barrier.getYPosition();
        double lengthBarrier = screenWidth/50;//barrier.getLength();
        double thicknessBarrier = barrier.getThickness();

        double cornerPointX = cornerPoint[0] + (isWest ? (-screenWidth/52) : (screenWidth/52));
        double cornerPointY = cornerPoint[1];

        boolean xFlag = false;
        boolean yFlag = false;
        boolean insideboundary = false;
       
        if(isWest){
            insideboundary = cornerPointX >= 0;
        }
        else{
            insideboundary = cornerPointX <= screenWidth;
        }

       // boolean insideBoundary = cornerPointX >= 0 && cornerPointX <= screenWidth;

       // if barrier is colliding with the  screen edges then this corner is colliding 
        if(!insideboundary){
            return true;
        }

        if (cornerPointX <= xBarrier + lengthBarrier && cornerPointX >= xBarrier){
            xFlag = true;
        }
        if (cornerPointY <= yBarrier + thicknessBarrier && cornerPointY >= yBarrier){
            yFlag = true;
            
        }

        if (xFlag && yFlag){
            return true;
        }

        return false;

    }


    void changeDirection(boolean isWest){
        if (isWest) {
            direction = RIGHT_DIRECTION;
        }
        else {
            direction = LEFT_DIRECTION;
        }
    }

    /**
     * This function checks if the current barrier collides with any other barrier
     * If so, cahnges the direction or keep the stiffness
     * Otherwise, start the movement
     *
     * @param barrierViews the list of barrierViews to check if the current barrier is colliding with another barrier
     */
    @Override
    public void checkCollision(List<BarrierView> barrierViews) {
        // TODO Auto-generated method stub
        double xPosition = barrier.getXPosition();
        double yPosition = barrier.getYPosition();

        double[][] WestCorners = {
            {xPosition, yPosition},
            {xPosition, yPosition + barrierThickness}
        };

        double[][] EastCorners = {
            {xPosition + barrierLength, yPosition},
            {xPosition + barrierLength, yPosition + barrierThickness}
        };



        boolean IsWestCollision = false;
        boolean IsEastCollision = false;


        //TODO Avoid collision of barrier with itself!!!
        int i = 0;
        while (i < barrierViews.size()) {
            Barrier b = barrierViews.get(i).getBarrier();

            int j = 0;
            while (j < WestCorners.length && !IsWestCollision) {
                double[] corner = WestCorners[j];
                if (calculateInterception(corner, b, true)) {
                    // Handle interception
                    IsWestCollision = true;
                }
                j++;
            }

            int k = 0;
            while (k < EastCorners.length && !IsEastCollision) {
                double[] cornerEast = EastCorners[k];
                if (calculateInterception(cornerEast, b, false)) {
                    // Handle interception
                    IsEastCollision = true;
                }
                k++;
            }

            i++;
        }


        //if there are two collisions on both sides keep stiffness
        if(IsEastCollision && IsWestCollision){
            stiffness = true;
        }
        //If there is one collision only on single side
        else if(IsEastCollision || IsWestCollision){
            stiffness = false;

            changeDirection(IsWestCollision);
            

        }
        //if no collision at all
        else{
            stiffness = false;
        }
    }





    





    
}

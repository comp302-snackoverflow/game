package tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;
import tr.edu.ku.comp302.ui.view.BarrierView;


/**
 * This strategy class calculates the movements and collisions of the horizontal moving barriers
 */
public class HorizontalMovement implements IMovementStrategy{


    private Barrier Barrier;
    private double ScreenWidth;
    private double ScreenHeight;
    double BarrierThickness;
    double BarrierLength;
    boolean stiffness = true;
    long previousTime = 0;
    long currentTime = 0;


    
    int speed;
     // 0 or 1, 0 meaning left, 1 meaning right !
    static final short RIGHT_DIRECTION = 1;
    static final short LEFT_DIRECTION = 0;
    short direction = RIGHT_DIRECTION;
    double probabilisticMove = 0;




    static final double BARRIER_SPEED = 0.5;
    /**
     * Initialize essential instances of this class
     * 
     * @param ScreenWidth
     * @param ScreenHeight
     * @param barrier the barrier itself
     */
    public HorizontalMovement(double ScreenWidth, double ScreenHeight, Barrier barrier){
        this.Barrier = barrier;
        this.ScreenWidth = ScreenWidth;
        this.ScreenHeight = ScreenHeight;
        BarrierLength = barrier.getLength();
        BarrierThickness = barrier.getThickness();
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
                Barrier.setSpeed(BARRIER_SPEED);
    
                if (direction == RIGHT_DIRECTION) {
                    Barrier.setXPosition(Barrier.getXPosition() + Barrier.getSpeed());
                }
    
                else {
                    Barrier.setXPosition(Barrier.getXPosition() - Barrier.getSpeed());
                }
    
            }

        }

        
    }



   

    /**
     * This funciton takes the array of positions x and y a corner and checks if its intercepting other barrier area
     *
     *  */
    boolean calculateInterception(double[] cornerPoint, Barrier barrier, boolean isWest ){

        double xBarrier = barrier.getXPosition();
        double yBarrier = barrier.getYPosition();
        double lengthBarrier = ScreenWidth/50;//barrier.getLength();
        double thicknessBarrier = barrier.getThickness();

        double cornerPointX = cornerPoint[0] + (isWest ? (-ScreenWidth/52) : (ScreenWidth/52));
        double cornerPointY = cornerPoint[1];

        boolean xFlag = false;
        boolean yFlag = false;
        boolean insideboundary = false;
       
        if(isWest){
            insideboundary = cornerPointX >= 0;
        }
        else{
            insideboundary = cornerPointX <= ScreenWidth;
        }

       // boolean insideBoundary = cornerPointX >= 0 && cornerPointX <= ScreenWidth;

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
    public void checkCollision(List<BarrierView> BarrierViews) {
        // TODO Auto-generated method stub
        double xPosition = Barrier.getXPosition();
        double yPosition = Barrier.getYPosition();

        double[][] WestCorners = {
            {xPosition, yPosition},
            {xPosition, yPosition + BarrierThickness}
        };

        double[][] EastCorners = {
            {xPosition + BarrierLength, yPosition},
            {xPosition + BarrierLength, yPosition + BarrierThickness}
        };



        boolean IsWestCollision = false;
        boolean IsEastCollision = false;


        //TODO Avoid collision of barrier with itself!!!
        int i = 0;
        while (i < BarrierViews.size()) {
            Barrier b = BarrierViews.get(i).getBarrier();

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

package tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;

public class HorizontalMovement implements IMovementStrategy{


    private Barrier Barrier;
    private double ScreenWidth;
    double BarrierThickness;
    double BarrierLength;
    boolean stiffness = true;
    int speed;
    short direction = RIGHT_DIRECTION; // 0 or 1, 0 meaning left, 1 meaning right !

    static final short RIGHT_DIRECTION = 1;
    static final short LEFT_DIRECTION = 0;



    /**
     *
     *
     * @param ScreenWidth
     * @param barrier the barrier that is modified or checked for collison
     */
    public HorizontalMovement(double ScreenWidth, Barrier barrier){
        this.Barrier = barrier;
        this.ScreenWidth = ScreenWidth;
        BarrierLength = barrier.getLength();
        BarrierThickness = barrier.getThickness();
    }


    /**
     * If the barrier is not stiff and the determined direction for movement is left,
     * moves with a speed of L/4 to the left, otherwise, the same logic applies to the right.
     */
    public void move(){


        if (!stiffness){
            Barrier.setSpeed(ScreenWidth/40);

            if (direction == RIGHT_DIRECTION) {
                Barrier.setXPosition(Barrier.getXPosition() + Barrier.getSpeed());
            }

            else {
                Barrier.setXPosition(Barrier.getXPosition() - Barrier.getSpeed());
            }

        }
    }



    /**
     * This fucntion checks if the current barrier collides with any other barrier
     * If so, cahnges the driection or keep the stiffness
     * Otherwise, start the movement
     *
     * @param barrierViews
     */

    void checkCollision(List<BarrierView> BarrierViews){


        double xPosition = Barrier.getXPosition();
        double yPosition = Barrier.getYPosition();

        double[][] westCorners = {
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
        while (i < barrierViews.size()) {
            Barrier b = barrierViews.get(i);

            int j = 0;
            while (j < westCorners.length && !IsWestCollision) {
                double[] corner = westCorners[j];
                if (calculateInterception(corner, b, true)) {
                    // Handle interception
                    IsWestCollision = true;
                }
                j++;
            }

            int k = 0;
            while (k < EastCorners.length && !IsEastCollision) {
                double[] corner = EastCorners[k];
                if (calculateInterception(corner, b, false)) {
                    // Handle interception
                    IsEastCollision = true;
                }
                k++;
            }

            i++;
        }


        //if there are two collisions on both sides keep stifness
        if(IsEastCollision && IsWestCollision){
            stiffness = true;
        }
        else{
            stiffness = false;
            changeDirection(IsWestCollision);


        }





    }



    /**
     * This funciton takes the array of positions x and y a corner and checks if its intercepting other barrier area
     *
     *  */
    boolean calculateInterception(double[] cornerPoint, Barrier barrier, boolean isWest ){

        double xBarrier = barrier.getXPosition();
        double yBarrier = barrier.getYPosition();
        double lengthBarrier = barrier.getLength();
        double thicknessBarrier = barrier.getThickness();

        double cornerPointX = cornerPoint[0] + (isWest ? (-ScreenWidth/40) : (ScreenWidth/40));
        double cornerPointY = cornerPoint[1];

        boolean xFlag = false;
        boolean yFlag = false;

        if (cornerPointX <= xBarrier + lengthBarrier || cornerPointX >= xBarrier ){
            xFlag = true;
        }
        if (cornerPointY <= yBarrier + thicknessBarrier || cornerPointY >= yBarrier ){
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





    





    
}

package tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;
import tr.edu.ku.comp302.ui.view.BarrierView;

public class CircularMovement implements IMovementStrategy {

    private Barrier Barrier;
    private double radius;
    private double centerX;
    private double centerY;
    private double angle;
    boolean stiffness = true;
    double BarrierThickness;
    double BarrierLength;

    private double screenWidth;
    static final short CLOCKWISE = 1;
    static final short COUNTER_CLOCKWISE= 0;
    short direction = CLOCKWISE;



    public CircularMovement(double screenWidth, double screenHeight, Barrier barrier) {
        this.Barrier = barrier;
        this.radius = 1.5 * barrier.getLength();
        this.centerX = barrier.getXPosition() + barrier.getLength() / 2;
        this.centerY = barrier.getYPosition() + 1.5 * screenWidth / 10;
        this.angle = 0; 
        this.stiffness = false; 
        this.screenWidth =  screenWidth;
        BarrierLength = barrier.getLength();
        BarrierThickness = barrier.getThickness();
    }

    @Override
    public void move() {
        if (!stiffness) {
            double newX = centerX + radius * Math.cos(angle) - Barrier.getLength() / 2;
            double newY = centerY + radius * Math.sin(angle) - 1.5 * screenWidth / 10;

            Barrier.setXPosition(newX);
            Barrier.setYPosition(newY);

            this.angle += Math.toRadians(0.5);
            if (angle >= 2 * Math.PI) {
                angle -= 2 * Math.PI; 
            }
        }
    }

    //OLD VERSION
    boolean calculateInterception(double[] cornerPoint, Barrier barrier, boolean isWest ){

        double xBarrier = barrier.getXPosition();
        double yBarrier = barrier.getYPosition();
        double lengthBarrier = screenWidth/50;
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

    //IMPLEMENTED 
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
        double[][] NorthCorners = {
            {xPosition, yPosition},
            {xPosition + BarrierLength, yPosition},
        };

        double[][] SouthCorners = {
            {xPosition, yPosition + BarrierThickness},
            {xPosition + BarrierLength, yPosition + BarrierThickness}
        };
        

        boolean IsWestCollision = false;
        boolean IsEastCollision = false;
        boolean IsNorthCollision = false;
        boolean IsSouthCollision = false;


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

            int l = 0;
            while (l < NorthCorners.length && !IsNorthCollision) {
                double[] cornerNorth = WestCorners[l];
                if (calculateInterception(cornerNorth, b, true)) {
                    // Handle interception
                    IsNorthCollision = true;
                }
                l++;
            }

            int m = 0;
            while (m < SouthCorners.length && !IsSouthCollision) {
                double[] cornerSouth = EastCorners[m];
                if (calculateInterception(cornerSouth, b, false)) {
                    // Handle interception
                    IsSouthCollision = true;
                }
                m++;
            }

            i++;
        }
        //if there are collisions from the sides at the same time
        if(IsEastCollision && IsWestCollision){
            stiffness = true;
        }
        else if(IsEastCollision || IsWestCollision){
            stiffness = false;

            //changeDirection(IsWestCollision);
            
        }


        //if there are collisions from above and bottom at the same time 
        if(IsSouthCollision && IsNorthCollision){
            stiffness = true;
        }
        else if(IsSouthCollision || IsNorthCollision){
            stiffness = false;

            //changeDirection(IsWestCollision);
            
        }
        

        //If there is one collision only on single side
        
        //if no collision at all
        if (!(IsWestCollision||IsEastCollision||IsEastCollision||IsNorthCollision)){
            stiffness = false;
        }
    }

    //CHANGE DIRECTION TO BE ADDED

}

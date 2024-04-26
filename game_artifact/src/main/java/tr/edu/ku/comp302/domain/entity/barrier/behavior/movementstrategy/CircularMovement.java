package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
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


    private double screenHeight;
    private double screenWidth;
    static final short CLOCKWISE = 1;
    static final short COUNTER_CLOCKWISE= 0;
    short direction = CLOCKWISE;


    static private final short NORTH_WEST_CORNER= 0;
    static private final short NORTH_EAST_CORNER= 1;
    static private final short SOUTH_WEST_CORNER= 2;
    static private final short SOUTH_EAST_CORNER= 3;

    public CircularMovement(double screenWidth, double screenHeight, Barrier barrier) {
        this.Barrier = barrier;
        this.radius = 1.5 * barrier.getLength();
        this.centerX = barrier.getXPosition() + barrier.getLength() / 2;
        this.centerY = barrier.getYPosition() + 1.5 * screenWidth / 10;
        this.angle = 0; 
        this.stiffness = false; 
        this.screenWidth =  screenWidth;
        this.screenHeight = screenHeight;
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
            Barrier.getBoundingBox().setRect(newX, newY, Barrier.getLength(), Barrier.getThickness());

            this.angle += Math.toRadians(0.5);
            if (angle >= 2 * Math.PI) {
                angle -= 2 * Math.PI; 
            }
        }
    }

    

    //IMPLEMENTED 
    @Override
    public void checkCollision(List<BarrierView> BarrierViews) {
        // TODO Auto-generated method stub

        double xPosition = Barrier.getXPosition();
        double yPosition = Barrier.getYPosition();

        double[] NorthWestCorner = {xPosition, yPosition};
        double[] NorthEastCorner = {xPosition + BarrierLength, yPosition};
        double[] SouthWestCorner = {xPosition, yPosition + BarrierThickness};
        double[] SouthEastCorner = {xPosition + BarrierLength, yPosition + BarrierThickness};
       
        
        

        boolean IsNorthCollision = false;
        boolean IsWestCollision = false;
        boolean IsSouthCollision = false;
        boolean IsEastCollision = false;


        

        //TODO Avoid collision of barrier with itself!!!
        int i = 0;
        while (i < BarrierViews.size()) {
            Barrier b = BarrierViews.get(i).getBarrier();
            boolean[] output;

            if (!Barrier.equals(b) && Barrier.getBoundingBox().intersects(b.getBoundingBox())) {
                stiffness = true;
                return;
            }
            
            if (!IsNorthCollision && !IsWestCollision) {
                
                
                output = calculateInterception(NorthWestCorner, b, NORTH_WEST_CORNER);

                IsNorthCollision = output[0];
                IsWestCollision = output[1];

            }

            if (!IsNorthCollision && !IsEastCollision) {
                
                
                output = calculateInterception(NorthEastCorner, b, NORTH_EAST_CORNER);

                IsNorthCollision = output[0];
                IsEastCollision = output[1];

            }

            if (!IsSouthCollision && !IsWestCollision) {
                
                
                output = calculateInterception(SouthWestCorner, b, SOUTH_WEST_CORNER);

                IsSouthCollision = output[0];
                IsWestCollision = output[1];

            }

            if (!IsSouthCollision && !IsEastCollision) {
                
                
                output = calculateInterception(SouthEastCorner, b, SOUTH_EAST_CORNER);

                IsSouthCollision = output[0];
                IsEastCollision = output[1];

            }

            i++;
        }

       // System.out.println(IsEastCollision + "__ EAST __ WEST __ " + IsWestCollision);
        //System.out.println(IsNorthCollision + "__ NORTH __ SOUTH __ " + IsSouthCollision);
        //if there are collisions from the sides at the same time
        if(IsEastCollision && IsWestCollision){
            stiffness = true;
        }
        if(IsEastCollision || IsWestCollision){
            stiffness = true;

            //changeDirection(IsWestCollision);
            
        }


        //if there are collisions from above and bottom at the same time 
        if(IsSouthCollision && IsNorthCollision){
            stiffness = true;
        }
        if(IsSouthCollision || IsNorthCollision){
            stiffness = false;

            //changeDirection(IsWestCollision);
            
        }
        

        //If there is one collision only on single side
        
        //if no collision at all
        else if (!(IsWestCollision||IsEastCollision||IsSouthCollision||IsNorthCollision)){
            stiffness = false;
        }

    }

    //OLD VERSION
    /**
     * 
     * @param cornerPoint
     * @param barrier
     * @param CornerNumber 0: NORTH WEST, 1: NORTH EAST, 2: SOUTH WEST, 3: SOUTH EAST
     * @return
     */
    boolean[] calculateInterception(double[] cornerPoint, Barrier barrier, short CornerNumber ){

        boolean horizontalCollision = false;
        boolean verticalCollision = false;

        double xBarrier = barrier.getXPosition();
        double yBarrier = barrier.getYPosition();
        double lengthBarrier = screenWidth/50;
        double thicknessBarrier = barrier.getThickness();

        double cornerPointX = cornerPoint[0];
        double cornerPointY = cornerPoint[1];

        double horizontalCornerPointX = cornerPointX + (CornerNumber % 2 == 0 ? (-screenWidth/52) : (screenWidth/52));
    
       

        double verticalCornerPointY = cornerPointY + (CornerNumber <= 1 ? (-screenWidth/52) : (screenWidth/52));


        //Calculate Horizontal Collision
        if (horizontalCornerPointX <= xBarrier + lengthBarrier && horizontalCornerPointX >= xBarrier){
            if (cornerPointY <= yBarrier + thicknessBarrier && cornerPointY >= yBarrier){
                horizontalCollision = true;
            }
        }
        
        //Calculate Vertical Collision
        if (cornerPointX <= xBarrier + lengthBarrier && cornerPointX >= xBarrier){
            if (verticalCornerPointY <= yBarrier + thicknessBarrier && verticalCornerPointY >= yBarrier){
                verticalCollision = true;
                
            }
        }

        //Calculate Horizontal Boundary Exceed
        
        if (horizontalCornerPointX >= screenWidth || horizontalCornerPointX <= 0){
            horizontalCollision = true;
        }

        //Calculate Vertical Boundary Exceed
        
        if (verticalCornerPointY >= screenHeight || verticalCornerPointY <= 0){
            verticalCollision = true;
        }
        
        boolean[] output = {verticalCollision, horizontalCollision};

        return output;

    }

    //CHANGE DIRECTION TO BE ADDED(?)


}

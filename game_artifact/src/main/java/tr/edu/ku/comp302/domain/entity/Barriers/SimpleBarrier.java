package tr.edu.ku.comp302.domain.entity.Barriers;

public class SimpleBarrier extends Barrier{

    public SimpleBarrier(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        //TODO Auto-generated constructor stub

        health = 1;
        
    }
    
}

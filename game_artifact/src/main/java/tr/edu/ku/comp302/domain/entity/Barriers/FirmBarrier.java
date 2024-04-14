package tr.edu.ku.comp302.domain.entity.Barriers;

public class FirmBarrier extends Barrier{
    



    public FirmBarrier(double xPosition, double yPosition, double screenWidth, double screenHeight) {
        super(xPosition, yPosition, screenWidth, screenHeight);
        //TODO Auto-generated constructor stub
        ImagePath = "/assets/firm_barrier.png";
        health = 3;

    }
    
}


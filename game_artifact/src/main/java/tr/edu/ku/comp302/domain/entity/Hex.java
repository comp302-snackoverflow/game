package tr.edu.ku.comp302.domain.entity;
import java.awt.geom.Rectangle2D;

import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;



public class Hex extends Entity{

    private int size = 8;
    private double speed = 0.2 * LanceOfDestiny.getScreenWidth(); // in px/s

    public Hex(double xPosition, double yPosition) {
        super(xPosition, yPosition);
        
        boundingBox = new Rectangle2D.Double(xPosition, yPosition, size, size);

    }

    @Override
    public void handleCollision(boolean isWall) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'handleCollision'");
    }
    

    /**
     * Moves the hex in the game
     */
    public void move() {
        yPosition += speed;
        boundingBox.setRect(xPosition, yPosition, size, size);
        actualShape.setFrame(xPosition, yPosition, size, size);
    }
}

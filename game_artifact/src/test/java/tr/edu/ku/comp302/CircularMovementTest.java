package tr.edu.ku.comp302;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy.CircularMovement;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class CircularMovementTest {
    private ExplosiveBarrier explosiveBarrier;
    private CircularMovement circularMovement;

    //creating an explosive barrier at x=500 y=500 to check its movements strategy
    @BeforeEach
    public void setUp() {
        explosiveBarrier = new ExplosiveBarrier(500, 500);
        circularMovement = new CircularMovement(explosiveBarrier);
    }
    /**
     * Black-Box Test
     * Testing whether the barrier moves when the direction is set to STIFF or not.
     */
    @Test
    public void testNoMovementWhenStiff() {
        //To ensure that the circularMovement is not null
        assertNotNull(circularMovement); 
        
        circularMovement.setDirection(CircularMovement.Direction.STIFF);
        double initialX = explosiveBarrier.getXPosition();
        double initialY = explosiveBarrier.getYPosition();

        // Moving the barrier with the speed specified in the project description
        circularMovement.move(explosiveBarrier, 5.0);

        // Checking whether the initial and new x and y positions of the barrier are the same after the movement
        assertEquals(initialX, explosiveBarrier.getXPosition());
        assertEquals(initialY, explosiveBarrier.getYPosition());
    }

    /**
     * Black-Box Test
     * Testing if the radius and center is properly initialized within the movement strategy.
     */
    @Test
    public void testInitializationOfCenterAndRadius() {
        //To ensure that the circularMovement is not null
        assertNotNull(circularMovement);
        
        // Verifying that the center and radius are initialized correctly
        double expectedCenterX = explosiveBarrier.getXPosition() + explosiveBarrier.getLength() / 2 - LanceOfDestiny.getScreenWidth() * 0.15 * Math.cos(circularMovement.getAngle());
        double expectedCenterY = explosiveBarrier.getYPosition() + explosiveBarrier.getThickness() / 2 + LanceOfDestiny.getScreenWidth() * 0.15 * Math.sin(circularMovement.getAngle());
        assertEquals(expectedCenterX, circularMovement.centerX, 0.001);
        assertEquals(expectedCenterY, circularMovement.centerY, 0.001);
        assertEquals(LanceOfDestiny.getScreenWidth() * 0.15, circularMovement.radius, 0.001);
    }

    /**
     * Glass-Box Test
     * Testing if the angle is updated correctly in the clockwise direction.
     */
    @Test
    public void testClockwiseAngleUpdate() {
        //To ensure that the circularMovement is not null
        assertNotNull(circularMovement); 
        
        //setting the rotation direction to clockwise
        circularMovement.setDirection(CircularMovement.Direction.CLOCKWISE);
        double initialAngle = circularMovement.getAngle();

        // Moving the barrier with the speed specified in the project description
        circularMovement.move(explosiveBarrier, 5.0);

        // Verifying that the angle has decreased
        assertTrue(circularMovement.getAngle() < initialAngle);
    }

    /**
     * Glass-Box Test
     * Testing if the angle is updated correctly in the counter-clockwise direction.
     */
    @Test
    public void testCounterClockwiseAngleUpdate() {
        //To ensure that the circularMovement is not null
        assertNotNull(circularMovement); 
        
        //setting the rotation direction to counter-clockwise
        circularMovement.setDirection(CircularMovement.Direction.COUNTER_CLOCKWISE);
        double initialAngle = circularMovement.getAngle();

        // Moving the barrier with the speed specified in the project description
        circularMovement.move(explosiveBarrier, 5.0);

        // Verifying that the angle has increased
        assertTrue(circularMovement.getAngle() > initialAngle);
    }

    /**
     * Glass-Box Test
     * Testing the normalization of the angle when it exceeds 2 * PI 
     */
    @Test
    public void testAngleNormalization() {
        //To ensure that the circularMovement is not null
        assertNotNull(circularMovement); 
        
        circularMovement.setDirection(CircularMovement.Direction.CLOCKWISE);

        //setting the angle to a value slightly higher than 2*PI
        circularMovement.setAngle(2 * Math.PI + 0.1); 

        // Moving the barrier with the speed specified in the project description
        circularMovement.move(explosiveBarrier, 5.0);

        // Verifying that the angle is normalized to be within 0 to 2 * PI
        assertTrue(circularMovement.getAngle() >= 0 && circularMovement.getAngle() < 2 * Math.PI);
    }

}


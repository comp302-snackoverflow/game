package tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;
import tr.edu.ku.comp302.ui.view.BarrierView;

/*public class CircularMovement implements IMovementStrategy{



    double radius;
    private Barrier barrier;
    private double centerX;
    private double centerY;
    private double angle;

    public CircularMovement(double ScreenWidth, double ScreenHeight, Barrier barrier) {
        this.radius = ScreenWidth * 0.15;  // Set radius to 15% of screen width
        this.barrier = barrier;
        // Assuming you want the circle's center to be at the screen's center
        this.centerX = ScreenWidth / 2;
        this.centerY = ScreenHeight / 2;
        this.angle = 0;  // Start angle at 0 radians
    }

    @Override
    public void move() {
        // Calculate the new X and Y positions based on the current angle and radius
        double newX = centerX + radius * Math.cos(angle);
        double newY = centerY + radius * Math.sin(angle);

        barrier.setXPosition(newX);
        barrier.setYPosition(newY);

        // Increment the angle by a small radians value, adjust this to control speed
        this.angle += Math.toRadians(0.2);  // Adjust this value to increase or decrease speed

        // Optional: Reset the angle to 0 when it completes a full circle to avoid overflow
        if (angle >= 2 * Math.PI) {
            angle -= 2 * Math.PI;
        }
    }

    @Override
    public void checkCollision(List<BarrierView> BarrierViews) {
        // Collision checking logic can be implemented here if needed
    }
}
*/

public class CircularMovement implements IMovementStrategy {

    private Barrier barrier;
    private double radius;
    private double centerX;
    private double centerY;
    private double angle;
    private boolean isStiff;

    private double screenWidth;

    public CircularMovement(double screenWidth, double screenHeight, Barrier barrier) {
        this.barrier = barrier;
        // Calculate the radius as 1.5 times the length of the barrier
        this.radius = 1.5 * barrier.getLength();
        // Set the center of the circle
        this.centerX = barrier.getXPosition() + barrier.getLength() / 2;
        this.centerY = barrier.getYPosition() + 1.5 * screenWidth / 10;
        this.angle = 0; // Start angle
        this.isStiff = false; // Initially stiff, can be updated based on space check
        this.screenWidth =  screenWidth;
    }

    @Override
    public void move() {
        if (!isStiff) {
            double newX = centerX + radius * Math.cos(angle) - barrier.getLength() / 2;
            double newY = centerY + radius * Math.sin(angle) - 1.5 * screenWidth / 10;

            barrier.setXPosition(newX);
            barrier.setYPosition(newY);

            // Slow rotation speed
            this.angle += Math.toRadians(0.5);
            if (angle >= 2 * Math.PI) {
                angle -= 2 * Math.PI; // Normalize the angle
            }
        }
    }

    @Override
    public void checkCollision(List<BarrierView> BarrierViews) {
        // Check for space around the barrier to decide if it should move
        return;
    }

    private boolean hasEnoughSpace(List<BarrierView> barrierViews) {
        // Implement logic to determine if there's enough space to move circularly
        // This might involve checking distances to other barriers and ensuring no overlap
        // For simplicity, let's assume it returns true if no barriers are within the radius distance
        for (BarrierView view : barrierViews) {
            Barrier other = view.getBarrier();
            if (other != barrier && Math.sqrt(Math.pow(other.getXPosition() - centerX, 2) + Math.pow(other.getYPosition() - centerY, 2)) < radius + other.getLength()) {
                return false; // Not enough space
            }
        }
        return true; // Enough space
    }
}

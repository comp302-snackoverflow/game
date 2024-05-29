package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;

import java.util.List;

public interface IMovementStrategy {
    /**
     * Returns the padding in x-axis for the barrier. This makes up the horizontal part of the barrier's
     * invisible hit box in barrier-barrier collisions
     * @return the padding in x-axis
     */
    double getXPadding();

    /**
     * Returns the padding in y-axis for the barrier. This makes up the vertical part of the barrier's
     * invisible hit box in barrier-barrier collisions
     * @return the padding in y-axis
     */
    double getYPadding();

    /**
     * Moves the barrier in the appropriate direction with the given speed.
     *
     * @param barrier the barrier to move
     * @param speed   speed of the barrier in px/update Should be calculated beforehand.
     */
    void move(Barrier barrier, double speed);

    /**
     * Changes the direction of the barrier or stop it completely if any barrier is close to it.
     *
     * @param barrier  barrier to be compared against others
     * @param barriers all barriers
     */
    void handleCloseCalls(Barrier barrier, List<Barrier> barriers);

    /**
     * Returns the direction of the barrier in x-axis.
     * @return -1 if the horizontal movement of the barrier is to the left, 1 if it is to the right, 0 otherwise.
     */
    int getXDirection();

    /**
     * Returns the direction of the barrier in y-axis. y-axis is inverted due to Swing's coordinate system.
     * @return -1 if the vertical movement of the barrier is to the bottom, 1 if it is to the up, 0 otherwise.
     */
    int getYDirection();

    /**
     * Changes the direction of the barrier to the opposite direction.
     */
    void turnBack();

    /**
     * Stops the movement of the barrier.
     */
    void stopMoving();

    /**
     * Starts the movement of the barrier. Randomly selects a direction within available directions.
     */
    void startMoving();

    /**
     * Adjusts the movement parameters of the barrier according to the given barrier.
     * Used in case the screen is resized.
     * @param barrier the barrier to take reference from
     */
    void adjustMovementParameters(Barrier barrier);
}

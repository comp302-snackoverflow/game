package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;

import java.util.List;

public interface IMovementStrategy {
    double getXPadding();

    double getYPadding();

    /**
     * Moves the Barrier self in the direction of the barrier with the given speed.
     *
     * @param barrier the barrier to move
     * @param speed   speed of the barrier in px/update Should be calculated beforehand.
     */
    void move(Barrier barrier, double speed);

    /**
     * Changes the direction of the barrier if any barrier is close to it.
     *
     * @param barrier  barrier to be compared against others
     * @param barriers all barriers
     */
    void handleCloseCalls(Barrier barrier, List<Barrier> barriers);
}

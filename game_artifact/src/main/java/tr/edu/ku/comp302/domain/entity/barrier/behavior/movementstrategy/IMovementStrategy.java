package tr.edu.ku.comp302.domain.entity.barrier.behavior.movementstrategy;

import java.util.List;

import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.ui.view.BarrierView;

public interface IMovementStrategy {
    void move();

    void checkCollision(List<Barrier> barriers);
}

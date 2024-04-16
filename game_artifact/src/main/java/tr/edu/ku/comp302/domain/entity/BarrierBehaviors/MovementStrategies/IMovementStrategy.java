package tr.edu.ku.comp302.domain.entity.BarrierBehaviors.MovementStrategies;

import java.util.List;

import tr.edu.ku.comp302.ui.view.BarrierView;

public interface IMovementStrategy {
    void move();

    void checkCollision(List<BarrierView> BarrierViews);
}

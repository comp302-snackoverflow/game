package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Barriers.Barrier;
import tr.edu.ku.comp302.ui.view.BarrierView;

import java.awt.geom.Ellipse2D;
import java.util.List;
import java.awt.geom.RectangularShape;
import java.awt.geom.Rectangle2D;

public class CollisionHandler {

    public static Entity testFireballEntityOverlap(FireBall fireBall, Entity entity) {
        Rectangle2D b1 = fireBall.getBoundingBox();
        Rectangle2D b2 = entity.getBoundingBox();
        if (!b1.intersects(b2)) {
            return null;
        }
        RectangularShape actualFireball = fireBall.getActualShape();
        RectangularShape actualEntity = entity.getActualShape();
        if (actualEntity instanceof Rectangle2D) {
            if (actualFireball.intersects((Rectangle2D) actualEntity)) {
                return entity;
            }
        } else if (actualEntity instanceof Ellipse2D) {
            double fireBallCenterX = actualFireball.getCenterX();
            double fireBallCenterY = actualFireball.getCenterY();

            double entityCenterX = actualEntity.getCenterX();
            double entityCenterY = actualEntity.getCenterY();

            double sumOfR = actualFireball.getWidth() / 2 + actualEntity.getWidth() / 2;
            double distanceSquared = Math.pow(fireBallCenterX - entityCenterX, 2) + Math.pow(fireBallCenterY - entityCenterY, 2);
            if (distanceSquared <= sumOfR * sumOfR) {
                return entity;
            }
        }

        return null;
    }

    public static Barrier testBarrierFireballOverlap(FireBall fireBall, List<BarrierView> barrierViews) {
        for (BarrierView barrierView : barrierViews) {
            Barrier barrier = barrierView.getBarrier();
            Entity overlap = testFireballEntityOverlap(fireBall, barrier);
            if (overlap != null) {
                return barrier;
            }
        }
        return null;
    }

    public static boolean hitLeftWall(FireBall fireBall) {
        return fireBall.getXPosition() <= 0;
    }

    public static boolean hitRightWall(FireBall fireBall, double width) {
        return fireBall.getXPosition() + fireBall.getSize() >= width;
    }

    public static boolean hitCeiling(FireBall fireBall) {
        return fireBall.getYPosition() <= 0;
    }

    public static boolean hitFloor(FireBall fireBall, double height) {
        return fireBall.getYPosition() + fireBall.getSize() >= height;
    }


    /**
     * 
     * This function calls the check collision function of every barrier.
     * 
     * */
    public static Barrier testBarrierBarrierOverlap(List<BarrierView> barrierViews) {
        for (BarrierView barrierView : barrierViews) {
            Barrier barrier = barrierView.getBarrier();
            barrier.checkCollison(barrierViews);
            
        }
        return null;
    }
}

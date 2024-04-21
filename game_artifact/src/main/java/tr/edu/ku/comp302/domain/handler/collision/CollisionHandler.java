package tr.edu.ku.comp302.domain.handler.collision;

import tr.edu.ku.comp302.domain.entity.Barrier;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.ui.view.BarrierView;
import tr.edu.ku.comp302.ui.view.FireBallView;
import tr.edu.ku.comp302.ui.view.LanceView;

import java.awt.*;
import java.util.List;
import java.awt.geom.RectangularShape;
import java.awt.geom.Rectangle2D;

public class CollisionHandler {
    /**
     * Tests if the fireball collides with the barrier.
     * Probably has some bugs (More like for sure)
     * Works in three steps:
     * First step check for bounding box collision since it's faster
     * Then, a narrower check is made. However, it's only different for the fireball
     * Finally, another step is made to determine the exact side of the collision
     *
     * @param fireBall the fireball object
     * @param barrier the barrier object
     * @return the side of the collision, or null if there is no collision
     * @throws CollisionError we're damned if we see this
     */
    public static Collision testFireballBarrierOverlap(FireBall fireBall, Barrier barrier) throws CollisionError {
        // We can also return the exact angle (hopefully correctly) if we want to
        // For now, returns the direction of the collision (again, hopefully correctly)
        Rectangle2D b1 = fireBall.getBoundingBox();
        Rectangle2D b2 = barrier.getBoundingBox();

        if (!b1.intersects(b2)) {
            return null;
        }

        RectangularShape actualFireball = fireBall.getActualShape();
        Rectangle2D rect = (Rectangle2D) barrier.getActualShape();

        if (!actualFireball.intersects(rect)) {
            return null;
        }

        double x1 = rect.getMinX();
        double y1 = rect.getMinY(); // top left corner

        double x2 = rect.getMinX() + rect.getWidth();
        double y2 = rect.getMinY(); // top right corner

        double x3 = rect.getMinX() + rect.getWidth();
        double y3 = rect.getMinY() + rect.getHeight(); // bottom right corner

        double x4 = rect.getMinX();
        double y4 = rect.getMinY() + rect.getHeight(); // bottom left corner

        int collision = 0;
        if (fireballIntersectsLine(fireBall, x1, y1, x2, y2)) {
            collision |= 0b0001;  // top segment
        }
        if (fireballIntersectsLine(fireBall, x2, y2, x3, y3)) {
            collision |= 0b0010;  // right segment
        }
        if (fireballIntersectsLine(fireBall, x3, y3, x4, y4)) {
            collision |= 0b0100; // bottom segment
        }
        if (fireballIntersectsLine(fireBall, x4, y4, x1, y1)) {
            collision |= 0b1000;  // left segment
        }


        return switch (collision) {
            case 0b0001, 0b1011 -> // 1011 => top, left, right. Reduced to top case.
                    Collision.TOP;
            // return 0;
            case 0b0010, 0b0111 -> // 0111 => top, right, bottom. Reduced to right case.
                    Collision.RIGHT;
            case 0b0100, 0b1110 -> // 1110 => left, right, bottom. Reduced to bottom case.
                    Collision.BOTTOM;
            case 0b1000, 0b1101 -> // 1101 => top, left, bottom. Reduced to left case.
                    Collision.LEFT;
            case 0b0011 -> Collision.TOP_RIGHT;
            case 0b0110 -> Collision.BOTTOM_RIGHT;
            case 0b1100 -> Collision.BOTTOM_LEFT;
            case 0b1001 -> Collision.TOP_LEFT;
            default -> // top and bottom; left and right; all 4 sides; no sides => should never happen
                    throw new CollisionError("How did we get here?");
        };
    }

    public static Collision testBarrierFireballOverlap(FireBall fireBall, List<BarrierView> barrierViews) throws CollisionError {
        for (BarrierView barrierView : barrierViews) {
            Barrier barrier = barrierView.getBarrier();
            Collision overlap = testFireballBarrierOverlap(fireBall, barrier);
            if (overlap != null) {
                return overlap;
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

    private static boolean fireballIntersectsLine(FireBall fireball, double x1, double y1, double x2, double y2) {
        double x = fireball.getXPosition();
        double y = fireball.getYPosition();
        double size = fireball.getSize();

        double centerX = x + size;
        double centerY = y + size;

        double minDistance = 2 * CollisionMath.triangleArea(x1, y1, x2, y2, centerX, centerY) / CollisionMath.lineLength(x1, y1, x2, y2);
        return minDistance <= size;
    }

    public static void checkCollisions(FireBallView fireBallView, LanceView lanceView) {
        FireBall fireBall = fireBallView.getFireBall();
        Lance lance = lanceView.getLance();
        Rectangle fireBallBounds = new Rectangle((int) fireBall.getXPosition(), (int) fireBall.getYPosition(), fireBall.getSize(), fireBall.getSize());
        //Rectangle lanceBounds = new Rectangle((int) lance.getXPosition(), (int) lance.getYPosition(), (int) lance.getLength(), (int) lance.getThickness());
        Rectangle lanceBounds = lance.getLanceBounds();

        if (fireBallBounds.intersects(lanceBounds)) { // fireball is intersecting the bounding box of the lance
            if (lance.getActualHitbox().intersects(fireBallBounds)) { // fireball is intersecting the actual hit box
                fireBall.handleReflection(lance.getRotationAngle());
            }
        }
    }

    public static void checkFireBallBorderCollisions(FireBallView fireBallView, int frameWidth, int frameHeight) {

        if (fireBallView.getFireBall().getXPosition() <= 0 ||
                fireBallView.getFireBall().getXPosition() + fireBallView.getFireBall().getSize() >= frameWidth) {
            fireBallView.getFireBall().bounceOffVerticalSurface();
        }
        if (fireBallView.getFireBall().getYPosition()<=0 ||
                fireBallView.getFireBall().getYPosition() + fireBallView.getFireBall().getSize() >= frameHeight) {
            fireBallView.getFireBall().bounceOffHorizontalSurface();
        }
    }


}


/*
else if (actualEntity instanceof Ellipse2D circle) {
            double fireBallCenterX = actualFireball.getCenterX();
            double fireBallCenterY = actualFireball.getCenterY();

            double entityCenterX = circle.getCenterX();
            double entityCenterY = circle.getCenterY();

            double sumOfR = actualFireball.getWidth() / 2 + actualEntity.getWidth() / 2;
            double distanceSquared = Math.pow(fireBallCenterX - entityCenterX, 2) + Math.pow(fireBallCenterY - entityCenterY, 2);
            if (distanceSquared <= sumOfR * sumOfR) {
                double angle = CollisionMath.getTangentAngle(fireBallCenterX, fireBallCenterY, actualFireball.getWidth() / 2,
                        entityCenterX, entityCenterY, circle.getWidth() / 2);
                // keeping it just to be safe
                // exploding barrier is a rectangle bruh
                if (angle < Math.PI / 8
                        && angle >= -Math.PI / 8) {
                    return Collision.RIGHT;
                }
                else if (angle < 3 * Math.PI / 8
                        && angle >= Math.PI / 8) {
                    return Collision.BOTTOM_RIGHT;
                } else if (angle < -Math.PI / 8
                        && angle >= -3 * Math.PI / 8){
                    return Collision.TOP_RIGHT;
                }
                else if (angle < 5 * Math.PI / 8
                        && angle >= 3 * Math.PI / 8) {
                    return Collision.BOTTOM;
                } else if (angle < -3 * Math.PI / 8
                        && angle >= -5 * Math.PI / 8) {
                    return Collision.TOP;
                }
                else if (angle < 7 * Math.PI / 8
                        && angle >= 5 * Math.PI / 8) {
                    return Collision.BOTTOM_LEFT;
                } else if (angle < -5 * Math.PI / 8
                        && angle >= -7 * Math.PI / 8) {
                    return Collision.TOP_LEFT;
                }
                else if (angle < -7 * Math.PI / 8
                        || angle >= 7 * Math.PI / 8) {
                    return Collision.LEFT;
                } else { // if we get here I quit
                    throw new CollisionError("How did we get here?");
                }
            }
        }
 */
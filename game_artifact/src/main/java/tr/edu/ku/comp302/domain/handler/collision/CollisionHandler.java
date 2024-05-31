package tr.edu.ku.comp302.domain.handler.collision;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.Remain;
import tr.edu.ku.comp302.domain.entity.SpellBox;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.util.Iterator;
import java.util.List;

public class CollisionHandler {
    private static final Logger logger = LogManager.getLogger(CollisionHandler.class);

    public static void checkFireballBarriersCollisions(FireBall fireBall, List<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            checkFireBallEntityCollisions(fireBall, barrier);
        }
    }

    public static boolean checkRemainLanceCollisions(Lance lance, Remain remain) {
        return remain.getBoundingBox().intersects(lance.getBoundingBox());
    }

    public static boolean checkSpellBoxLanceCollisions(Lance lance, SpellBox spellBox) {
        return spellBox.getBoundingBox().intersects(lance.getBoundingBox());
    }

    /**
     * Check if the target collides with any of the barriers in the list. Includes vertical and horizontal padding that
     * is applied differently depending on the target barrier's movement strategy.
     *
     * @param target   the barrier to tes
     * @param barriers list of all barriers.
     * @return a 4-bit integer, where bits from least to most significant represent collisions
     * with the top, right, bottom, and left sides of the target.
     */
    public static int checkCloseCalls(Barrier target, List<Barrier> barriers) {
        return checkCloseCalls(target, barriers, target.getExtendedHitbox());
    }

    public static int checkCloseCalls(Barrier target, List<Barrier> barriers, RectangularShape shape) {
        return switch (shape) {
            case Rectangle2D rect -> checkCloseCalls(target, barriers, rect);
            case Ellipse2D ellipse -> checkCloseCalls(target, barriers, ellipse);
            default -> {
                logger.warn("Unknown shape type: {}", shape.getClass().getName());
                yield 0b1111;
            }
        };
    }

    private static int checkCloseCalls(Barrier target, List<Barrier> barriers, Ellipse2D ellipse) {
        int sides = 0; // the bit order is `lbrt`

        if (ellipse.getX() <= 0) {
            sides |= 0b1000; // left side
        }
        if (ellipse.getX() + ellipse.getWidth() >= LanceOfDestiny.getScreenWidth() * 0.98) {
            sides |= 0b0010; // right side
        }
        if (ellipse.getY() <= 0) {
            sides |= 0b0001; // top side
        }
        if (ellipse.getY() + ellipse.getHeight() >= LanceOfDestiny.getScreenHeight() >> 1) {
            sides |= 0b0100; // bottom side
        }

        for (Barrier b : barriers) {
            if (b == target) {
                continue;
            }
            if (sides == 0b1111) {
                break;
            }
            Rectangle2D box = b.getBoundingBox();

            if (ellipse.intersects(box)) {
                int outcode = ellipseOutcode(ellipse, box.getCenterX(), box.getCenterY());
                sides |= outcode;
            }
        }
        return sides;
    }

    private static int checkCloseCalls(Barrier target, List<Barrier> barriers, Rectangle2D rect) {
        int sides = 0; // the bit order is `lbrt`

        if (rect.getMinX() <= 0) {
            sides |= 0b1000; // left side
        }
        if (rect.getMaxX() >= LanceOfDestiny.getScreenWidth() * 0.98) {
            sides |= 0b0010; // right side
        }
        if (rect.getMinY() <= 0) {
            sides |= 0b0001; // top side
        }
        if (rect.getMaxY() >= LanceOfDestiny.getScreenHeight() >> 1) {
            sides |= 0b0100; // bottom side
        }

        for (Barrier barrier : barriers) {
            if (barrier == target) {
                continue;
            }
//            if (barrier.getBoundingBox().intersects(target.getBoundingBox())) {
//                logger.warn("checkCloseCalls: Barrier bounding boxes intersect");
//            }

            if (sides == 0b1111) {
                break; // all sides are already collided
            }
            Rectangle2D box = barrier.getBoundingBox();

            if (rect.intersects(box)) {
                sides |= rectangleOutcode(rect, box.getCenterX(), box.getCenterY());
            }
        }
        return sides;
    }

    public static void checkFireBallBorderCollisions(FireBall fireBall, int frameWidth, int frameHeight) {
        if (fireBall.getXPosition() <= 0 || fireBall.getXPosition() + fireBall.getSize() >= frameWidth) {
            fireBall.bounceOffVerticalSurface();
        }
        if (fireBall.getYPosition() <= 0 || fireBall.getYPosition() + fireBall.getSize() >= frameHeight) {
            fireBall.bounceOffHorizontalSurface();
        }
    }

    public static boolean checkFireBallEntityCollisions(FireBall fireBall, Entity entity) {
        switch (entity) {
            case Lance lance -> {
                if (boundingBoxCollision(fireBall, lance)) {
                    try {
                        Collision side = hitBoxCollision(fireBall, lance);
                        if (side != null) {
                            resolveCollision(fireBall, lance, side);
                            return true;
                        }
                    } catch (CollisionError ignored) {
                    }
                }
            }

            case Barrier barrier -> {
                if (boundingBoxCollision(fireBall, barrier)) {
                    try {
                        Collision side = hitBoxCollision(fireBall, barrier);
                        if (side != null) {
                            if (!fireBall.isOverwhelming()) {
                                resolveCollision(fireBall, barrier, side);
                                return true;
                            } else {
                                if (barrier.isFrozen()) {
                                    resolveCollision(fireBall, barrier, side);
                                }
                                barrier.decreaseHealth();
                            }
                        }
                    } catch (CollisionError ignored) {
                    }
                }
            }
            case Entity ignored -> logger.warn("checkFireBallEntityCollisions: Unknown entity type");
        }
        return false;
    }

    private static int findCollisions(FireBall fireBall, Point topLeft, Point topRight, Point bottomRight, Point bottomLeft) {
        int collision = 0;
        if (fireballIntersectsLine(fireBall, topLeft, topRight)) {
            collision |= 0b0001;  // top segment
        }
        if (fireballIntersectsLine(fireBall, topRight, bottomRight)) {
            collision |= 0b0010;  // right segment
        }
        if (fireballIntersectsLine(fireBall, bottomRight, bottomLeft)) {
            collision |= 0b0100; // bottom segment
        }
        if (fireballIntersectsLine(fireBall, bottomLeft, topLeft)) {
            collision |= 0b1000;  // left segment
        }

        return collision;
    }

    private static Collision getCollisionSide(int collision) throws CollisionError {
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
            default -> throw new CollisionError("How did we get here?");
            // top and bottom; left and right; all 4 sides; no sides => should never happen
            // throw new CollisionError("How did we get here?");
        };
    }

    private static boolean fireballIntersectsLine(FireBall fireBall, Point p1, Point p2) {
        double x = fireBall.getXPosition();
        double y = fireBall.getYPosition();
        double radius = (double) fireBall.getSize() / 2;

        double centerX = x + radius;
        double centerY = y + radius;

        double minDistance = 2 * CollisionMath.triangleArea(p1.x, p1.y, p2.x, p2.y, centerX, centerY) / CollisionMath.lineLength(p1.x, p1.y, p2.x, p2.y);
        return minDistance <= radius;
    }

    private static Collision findCollisionSide(FireBall fireBall, Polygon hitBox) throws CollisionError {
        int[] xPoints = hitBox.xpoints;
        int[] yPoints = hitBox.ypoints;
        Point[] points = new Point[4];
        for (int i = 0; i < xPoints.length; i++) {
            points[i] = new Point(xPoints[i], yPoints[i]);
        }

        int collision = findCollisions(fireBall, points[0], points[1], points[2], points[3]);

        if (collision == 0) {
            return null;
        }
        return getCollisionSide(collision);
    }

    private static boolean boundingBoxCollision(FireBall fireBall, Entity entity) {
        Rectangle2D fireballBounds = fireBall.getBoundingBox();
        Rectangle2D entityBounds = entity.getBoundingBox();
        return fireballBounds.intersects(entityBounds);
    }

    private static Collision hitBoxCollision(FireBall fireball, Lance lance) throws CollisionError {
        return findCollisionSide(fireball, lance.getActualHitbox());
    }

    private static Collision hitBoxCollision(FireBall fireball, Barrier barrier) throws CollisionError {
        Point tl = new Point((int) barrier.getXPosition(), (int) barrier.getYPosition());
        Point tr = new Point((int) (barrier.getXPosition() + barrier.getLength()), (int) barrier.getYPosition());
        Point br = new Point((int) (barrier.getXPosition() + barrier.getLength()), (int) (barrier.getYPosition() + barrier.getThickness()));
        Point bl = new Point((int) barrier.getXPosition(), (int) (barrier.getYPosition() + barrier.getThickness()));

        return getCollisionSide(findCollisions(fireball, tl, tr, br, bl));
    }

    private static void resolveCollision(FireBall fireBall, Lance lance, Collision side) {
        switch (side) {
            case TOP, BOTTOM, LEFT, RIGHT -> fireBall.handleReflection(-lance.getRotationAngle(), lance.getDirection());
            case TOP_LEFT, BOTTOM_RIGHT, TOP_RIGHT, BOTTOM_LEFT ->
                    fireBall.handleCornerReflection(-lance.getRotationAngle(), lance.getDirection(), side);
        }
    }

    private static void resolveCollision(FireBall fireBall, Barrier barrier, Collision side) {
        // FIXME surface speed should be barrier.getYDirection when collision side is left or right
        //  think about the corner collision cases. @Omer-Burak-Duran
            switch (side) {
                case TOP, BOTTOM, LEFT, RIGHT -> fireBall.handleReflection(0, barrier.getXDirection());
                case TOP_LEFT, BOTTOM_RIGHT, TOP_RIGHT, BOTTOM_LEFT ->
                        fireBall.handleCornerReflection(0, barrier.getXDirection(), side);
            }
        if(fireBall.isOverwhelming() || !barrier.isFrozen()) {
            barrier.decreaseHealth();
        }
    }

    private static int ellipseOutcode(Ellipse2D ellipse, double centerX, double centerY) {
        double ellipseCenterX = ellipse.getCenterX();
        double ellipseCenterY = ellipse.getCenterY();
        double pi = Math.PI;
        int out = 0;

        // multiplying with -1 because Y Axis is inverted in Swing
        double lineAngle = -1 * Math.atan2((centerY - ellipseCenterY), (centerX - ellipseCenterX));

        if (centerY < ellipseCenterY && pi / 6 < lineAngle && lineAngle < 5 * pi / 6) { // point is above the ellipse
            out |= 0b0001; // top segment
        }
        if (centerX > ellipseCenterX && -pi / 3 < lineAngle && lineAngle < pi / 3) { // point is to the Right of the ellipse
            out |= 0b0010; // right segment
        }
        if (centerY > ellipseCenterY && -5 * pi / 6 < lineAngle && lineAngle < -pi / 6) { // point is below the ellipse
            out |= 0b0100; // bottom segment
        }
        if (centerY < ellipseCenterY && 2 * pi / 3 < lineAngle || lineAngle < -2 * pi / 3) { // point is to the Left of the ellipse
            out |= 0b1000; // left segment
        }

        return out;
    }

    /**
     * Acts similar to Rectangle2D.outcode method. However, unlike it, this method
     * calculates outcode based on the center of the rectangle.
     * @param rect the rectangle to calculate outcode for
     * @param centerX the x coordinate of the point to calculate outcode for
     * @param centerY the y coordinate of the point to calculate outcode for
     * @return a 4-bit integer, where bits from least to most significant bit represent top, right, bottom, and left sides.
     */
    private static int rectangleOutcode(Rectangle2D rect, double centerX, double centerY) {
        double rectCenterX = rect.getCenterX();
        double rectCenterY = rect.getCenterY();

        int out = 0;
        if (centerY < rectCenterY) {
            out |= 0b0001; // top segment
        }
        if (centerY > rectCenterY) {
            out |= 0b0100; // bottom segment
        }
        if (centerX > rectCenterX) {
            out |= 0b0010; // right segment
        }
        if (centerX < rectCenterX) {
            out |= 0b1000; // left segment
        }

        return out;
    }


    public static void handleHexCollision(List<Hex> hexes, List<Barrier> barriers) {
        if(hexes == null || barriers == null) {
            throw new NullPointerException();

        }

        Iterator<Hex> hexIterator = hexes.iterator();
        while (hexIterator.hasNext()) {
            Hex currentHex = hexIterator.next();
            for (Barrier barrier : barriers) {
                if (isCollidingWith(currentHex, barrier)) {
                    hexIterator.remove();
                    barrier.decreaseHealth();

                    System.out.println("Hex collided with barrier");
                    break;
                }
            }
        }
    }

    public static boolean isCollidingWith(Entity entity1, Entity entity2) {
        return entity1.getBoundingBox().intersects(entity2.getBoundingBox());
    }

    public static boolean checkBarrierCollisionWithBarriers(Barrier barrier, List<Barrier> barriersOnMap){
        for (Barrier b : barriersOnMap) {
            if (b != barrier && checkBarrierCollision(b, barrier)) {
                return true;
            }
        }
        return false;
    }
    private static boolean checkBarrierCollision(Barrier b1, Barrier b2){
        return b1.getBoundingBox().intersects(b2.getBoundingBox());
    }
}

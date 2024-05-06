package tr.edu.ku.comp302.domain.handler.collision;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.entity.Entity;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.*;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

import java.awt.*;
import java.util.List;
import java.awt.geom.RectangularShape;
import java.awt.geom.Rectangle2D;

public class CollisionHandler {
    private static Logger logger = LogManager.getLogger(CollisionHandler.class);

    public static void checkFireballBarriersCollisions(FireBall fireBall, List<Barrier> barriers) {
        for (Barrier barrier : barriers) {
            checkFireBallEntityCollisions(fireBall, barrier);
        }
    }

    /**
     * Check if the target collides with any of the barriers in the list. Includes vertical and horizontal padding
     * to the hitbox of the target.
     * @param target    the barrier to tes
     * @param barriers  list of all barriers.
     * @return a 4-bit integer, where bits from least to most significant represent collisions
     * with the top, right, bottom, and left sides of the target.
     */
    public static int checkCloseCalls(Barrier target, List<Barrier> barriers, double padX, double padY) {
        Rectangle2D rect = target.getBoundingBox(); // same as the actual hitbox
        Rectangle2D wider = new Rectangle2D.Double(rect.getX() - padX, rect.getY(),
                rect.getWidth() + 2 * padX, rect.getHeight());
        Rectangle2D taller = new Rectangle2D.Double(rect.getX(), rect.getY() - padY,
                rect.getWidth(), rect.getHeight() + 2 * padY);

        int sides = 0; // the bit order is `lbrt`

        if (target.getXPosition() <= padX) {
            sides |= 0b1000; // left side
        }
        if (target.getXPosition() + target.getLength() >= LanceOfDestiny.getScreenWidth() - padX) {
            sides |= 0b0010; // right side
        }
        if (target.getYPosition() <= padY) {
            sides |= 0b0001; // top side
        }
        if (target.getYPosition() + target.getThickness() >= LanceOfDestiny.getScreenHeight() - padY) {
            sides |= 0b0100; // bottom side
            logger.warn("checkCloseCalls: Barrier is at the bottom of the screen");
        }

        for (Barrier barrier : barriers) {
            if (barrier == target) {
                continue;
            }
            if (sides == 0b1111) {
                break; // all sides are already collided
            }
            Rectangle2D box = barrier.getBoundingBox();

            if (wider.intersects(box)) {
                int outcode = wider.outcode(box.getCenterX(), box.getCenterY());
                if ((outcode & Rectangle2D.OUT_LEFT) != 0) {
                    sides |= 0b1000;
                }
                if ((outcode & Rectangle2D.OUT_RIGHT) != 0) {
                    sides |= 0b0010;
                }
                if ((outcode & Rectangle2D.OUT_TOP) != 0) {
                    logger.warn("checkCloseCalls: Horizontal padded hit box top intersects");
                    logger.warn("Wider rect: " + wider);
                    logger.warn("Barrier: " + box);
                }
                if ((outcode & Rectangle2D.OUT_BOTTOM) != 0) {
                    logger.warn("checkCloseCalls: Horizontal padded hit box bottom intersects");
                    logger.warn("Wider rect: " + wider);
                    logger.warn("Barrier: " + box);
                }
            }
            if (taller.intersects(box)) {
                int outcode = taller.outcode(box.getCenterX(), box.getCenterY());
                if ((outcode & Rectangle2D.OUT_TOP) != 0) {
                    sides |= 0b0001;
                }
                if ((outcode & Rectangle2D.OUT_BOTTOM) != 0) {
                    sides |= 0b0100;
                }
                if ((outcode & Rectangle2D.OUT_LEFT) != 0) {
                    logger.warn("checkCloseCalls: Vertical padded hit box left intersects");
                    logger.warn("Taller rect: " + taller);
                    logger.warn("Barrier: " + box);
                }
                if ((outcode & Rectangle2D.OUT_RIGHT) != 0) {
                    logger.warn("checkCloseCalls: Vertical padded hit box right intersects");
                    logger.warn("Taller rect: " + taller);
                    logger.warn("Barrier: " + box);
                }
            }
        }
        return sides;
    }

    public static void checkFireBallBorderCollisions(FireBall fireBall, int frameWidth, int frameHeight) {
        if (fireBall.getXPosition() <= 0 ||
                fireBall.getXPosition() + fireBall.getSize() >= frameWidth) {
            fireBall.bounceOffVerticalSurface();
        }
        if (fireBall.getYPosition() <= 0 ||
                fireBall.getYPosition() + fireBall.getSize() >= frameHeight) {
            fireBall.bounceOffHorizontalSurface();
        }
    }

    public static void checkFireBallEntityCollisions(FireBall fireBall, Entity entity) {
        switch (entity) {
            case Lance lance -> {
                if (boundingBoxCollision(fireBall, lance)) {
                    try {
                        Collision side = hitBoxCollision(fireBall, lance);
                        if (side != null) {
                            resolveCollision(fireBall, lance, side);
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
                            resolveCollision(fireBall, barrier, side);
                        }
                    } catch (CollisionError ignored) {}
                }
            }
            case Entity ignored ->
                logger.warn("checkFireBallEntityCollisions: Unknown entity type");
        }
    }

    private static int findCollisions(FireBall fireBall, Point topLeft, Point topRight,
                                      Point bottomRight, Point bottomLeft) {
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
            case TOP, BOTTOM, LEFT, RIGHT -> fireBall.handleReflection(lance.getRotationAngle());
            case TOP_LEFT, BOTTOM_RIGHT, TOP_RIGHT, BOTTOM_LEFT ->
                    fireBall.handleCornerReflection(lance.getRotationAngle(), side);
        }
    }

    private static void resolveCollision(FireBall fireBall, Barrier barrier, Collision side) {
        switch (side) {
            case TOP, BOTTOM, LEFT, RIGHT -> fireBall.handleReflection(0, barrier.getDirection());
            case TOP_LEFT, BOTTOM_RIGHT, TOP_RIGHT, BOTTOM_LEFT ->
                    fireBall.handleCornerReflection(0, barrier.getDirection(), side);
        }

        barrier.decreaseHealth();
    }
}

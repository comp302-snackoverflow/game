package tr.edu.ku.comp302;

import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;

import tr.edu.ku.comp302.domain.entity.FireBall;

public class HandleReflectionTest {

    private FireBall fireBall;
    private double surfaceAngle;
    private double surfaceSpeed;

    // Initialize the FireBall position and speed.
    @Before
    public void setUp() {
        fireBall = new FireBall(0, 0);
        fireBall.setDx(0);
        fireBall.setDy(0);
    }

    // Rotation and speed of the surface is zero.
    // Should reflect normally with respect to the 90 degrees normal line.
    @Test
    public void testStationaryHorizontalReflection() {
        // the surface is horizontal and stationary
        surfaceAngle = 0;
        surfaceSpeed = 0;

        // FireBall is moving towards bottom right
        fireBall.setDx(3);
        fireBall.setDy(4);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        // new dx, dy should be +3, -4 respectively
        Assert.assertEquals(3, fireBall.getDx(), 0.001);
        Assert.assertEquals(-4, fireBall.getDy(), 0.001);
    }

    // Rotation is non-zero and speed of the surface is zero.
    // Should reflect normally with respect to the normal line of the rotated surface.
    @Test
    public void testStationaryRotatedReflection() {
        // the surface is rotated (45 degrees clockwise) and stationary
        surfaceAngle = 45;
        surfaceSpeed = 0;

        // FireBall is moving horizontally towards right
        fireBall.setDx(1);
        fireBall.setDy(0);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        // new dx, dy should be 0, -1 respectively
        Assert.assertEquals(0, fireBall.getDx(), 0.001);
        Assert.assertEquals(-1, fireBall.getDy(), 0.001);
    }


    // Rotation is zero and speed of the surface is non-zero.
    // X Speed of the surface is in the same direction with FireBall dx.
    // Should reflect normally with respect to the 90 degrees normal line.
    // Total Speed of the FireBall should increase by 5.
    @Test
    public void testMovingHorizontalReflection() {
        double dx, dy, oldSpeed, newSpeed;

        // the surface is horizontal and moving towards right
        surfaceAngle = 0;
        surfaceSpeed = 1;

        // FireBall is moving towards bottom right
        // total speed is 5
        fireBall.setDx(3);
        fireBall.setDy(4);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        oldSpeed = Math.sqrt(dx * dx + dy * dy);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        newSpeed = Math.sqrt(dx * dx + dy * dy);

        // new dx, dy should be 6, -8 respectively
        // total speed is now 10, still the same angle
        Assert.assertEquals(6, fireBall.getDx(), 0.001);
        Assert.assertEquals(-8, fireBall.getDy(), 0.001);

        // newSpeed should be 5 more than oldSpeed
        Assert.assertEquals(oldSpeed + 5, newSpeed, 0.001);
    }

    // Rotation and speed of the surface is non-zero.
    // X Speed of the surface is in the same direction with FireBall dx.
    // Should reflect normally with respect to the normal line of the rotated surface.
    // Total Speed of the FireBall should increase by 5.
    @Test
    public void testMovingRotatedReflection() {
        double dx, dy, oldSpeed, newSpeed;

        // the surface is rotated (45 degrees clockwise) and moving towards right
        surfaceAngle = 45;
        surfaceSpeed = 1;

        // FireBall is moving towards bottom right
        // total speed is 5
        fireBall.setDx(3);
        fireBall.setDy(4);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        oldSpeed = Math.sqrt(dx * dx + dy * dy);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        newSpeed = Math.sqrt(dx * dx + dy * dy);

        // new dx, dy should be -8, -6 respectively
        // total speed is now 10, with different angle
        Assert.assertEquals(-8, fireBall.getDx(), 0.001);
        Assert.assertEquals(-6, fireBall.getDy(), 0.001);

        // newSpeed should be 5 more than oldSpeed
        Assert.assertEquals(oldSpeed + 5, newSpeed, 0.001);
    }

    // Rotation is zero and speed of the surface is non-zero.
    // X Speed of the surface is in the opposite direction with FireBall dx.
    // Should reflect in a mirrored manner.
    // Total Speed should not change.
    @Test
    public void testOppositeMovingHorizontalReflection() {
        double dx, dy, oldSpeed, newSpeed;

        // the surface is horizontal and moving towards left
        surfaceAngle = 0;
        surfaceSpeed = -1;

        // FireBall is moving towards bottom right
        // total speed is 5
        fireBall.setDx(3);
        fireBall.setDy(4);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        oldSpeed = Math.sqrt(dx * dx + dy * dy);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        newSpeed = Math.sqrt(dx * dx + dy * dy);

        // new dx, dy should be -3, -4 respectively
        Assert.assertEquals(-3, fireBall.getDx(), 0.001);
        Assert.assertEquals(-4, fireBall.getDy(), 0.001);

        // total speed should remain unchanged
        Assert.assertEquals(oldSpeed, newSpeed, 0.001);
    }

    // Rotation and speed of the surface is non-zero.
    // X Speed of the surface is in the opposite direction with FireBall dx.
    // Should reflect in a mirrored manner.
    // Total Speed should not change.
    @Test
    public void testOppositeMovingRotatedReflection() {
        double dx, dy, oldSpeed, newSpeed;

        // the surface is rotated (45 degrees clockwise) and moving towards left
        surfaceAngle = 45;
        surfaceSpeed = -1;

        // FireBall is moving towards bottom right
        // total speed is 5
        fireBall.setDx(3);
        fireBall.setDy(4);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        oldSpeed = Math.sqrt(dx * dx + dy * dy);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        newSpeed = Math.sqrt(dx * dx + dy * dy);

        // new dx, dy should be -3, -4 respectively
        Assert.assertEquals(-3, fireBall.getDx(), 0.001);
        Assert.assertEquals(-4, fireBall.getDy(), 0.001);

        // total speed should remain unchanged
        Assert.assertEquals(oldSpeed, newSpeed, 0.001);
    }

    // Rotation is zero and speed of the surface is non-zero.
    // X Speed of the surface is perpendicular to FireBall dx.
    // Should reflect in 45 degrees rotated manner.
    // Total Speed should not change.
    @Test
    public void testPerpendicularReflection() {
        double dx, dy, oldSpeed, newSpeed;

        // the surface is horizontal and moving towards right
        surfaceAngle = 0;
        surfaceSpeed = 1;

        // FireBall is moving towards bottom
        // total speed is 1
        fireBall.setDx(0);
        fireBall.setDy(1);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        oldSpeed = Math.sqrt(dx * dx + dy * dy);

        fireBall.handleReflection(surfaceAngle, surfaceSpeed);

        dx = fireBall.getDx();
        dy = fireBall.getDy();
        newSpeed = Math.sqrt(dx * dx + dy * dy);

        // new dx, dy should be 1/sqrt(2) and -1/sqrt(2) respectively
        Assert.assertEquals(1/Math.sqrt(2), fireBall.getDx(), 0.001);
        Assert.assertEquals(-1/Math.sqrt(2), fireBall.getDy(), 0.001);

        // total speed should remain unchanged
        Assert.assertEquals(oldSpeed, newSpeed, 0.001);
    }

}

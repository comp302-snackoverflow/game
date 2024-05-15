package tr.edu.ku.comp302;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import tr.edu.ku.comp302.domain.entity.FireBall;

public class handleReflectionTest  {

    private FireBall fireball;

    @Before
    public void setUp() {
        fireball = new FireBall(0, 0);
    }

    @Test
    public void testStationarySurfaceReflection() {
        fireball.setDx(1);
        fireball.setDy(0);
        fireball.handleReflection(90, 0);
        assertEquals(0, fireball.getDx(), 0.0001);
        assertEquals(1, fireball.getDy(), 0.0001);
    }

    @Test
    public void testMovingSurfaceOppositeDirectionHighSpeed() {
        fireball.setDx(1);
        fireball.setDy(0);
        fireball.handleReflection(90, -2);
        assertEquals(-1, fireball.getDx(), 0.0001);
        assertEquals(0, fireball.getDy(), 0.0001);
    }

    @Test
    public void testMovingSurfaceSameDirection() {
        fireball.setDx(1);
        fireball.setDy(0);
        fireball.handleReflection(90, 1);
        assertEquals(6, fireball.getDx(), 0.0001);
        assertEquals(0, fireball.getDy(), 0.0001);
    }

    @Test
    public void testMovingSurfaceOppositeDirection() {
        fireball.setDx(1);
        fireball.setDy(0);
        fireball.handleReflection(90, -1);
        assertEquals(-1, fireball.getDx(), 0.0001);
        assertEquals(0, fireball.getDy(), 0.0001);
    }

    @Test
    public void testEdgeCaseZeroDegrees() {
        fireball.setDx(1);
        fireball.setDy(1);
        fireball.handleReflection(0, 0);
        assertEquals(1, fireball.getDx(), 0.0001);
        assertEquals(-1, fireball.getDy(), 0.0001);
    }

    @Test
    public void testZeroInitialSpeed() {
        fireball.setDx(0);
        fireball.setDy(0);
        fireball.handleReflection(45, 0);
        assertEquals(0, fireball.getDx(), 0.0001);
        assertEquals(0, fireball.getDy(), 0.0001);
    }
}


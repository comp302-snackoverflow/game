package tr.edu.ku.comp302;
import org.junit.jupiter.api.*;
import tr.edu.ku.comp302.domain.entity.FireBall;
import tr.edu.ku.comp302.domain.entity.Lance;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.collision.CollisionHandler;
import tr.edu.ku.comp302.domain.lanceofdestiny.LanceOfDestiny;

public class EntityTest {
    private Lance lance;
    private FireBall fireball;
    private ExplosiveBarrier explosiveBarrier;
    private SimpleBarrier simpleBarrier;

    @BeforeEach
    public void setUp() {
        lance = new Lance(1,2);
        fireball = new FireBall(4,5);
        explosiveBarrier = new ExplosiveBarrier(6,7);
        simpleBarrier = new SimpleBarrier(10,10);

    }


    @Test
    public void constructorsTest() {
        Assertions.assertTrue(lance.repOk(), "The lance's rep invariant should hold when it's initialized!");
        Assertions.assertTrue(fireball.repOk(), "The fireball's rep invariant should hold when it's initialized!");
        Assertions.assertTrue(explosiveBarrier.repOk(), "The explosive barrier's rep invariant should hold when it's initialized!");
        Assertions.assertTrue(simpleBarrier.repOk(), "The simple barrier's rep invariant should hold when it's initialized!");
    }
    @Test
    public void lanceMovementTest() {
        lance.setDirection(1);
        for (int i = 1; i<100; i++) {
            lance.updateXPosition(2);
            Assertions.assertTrue(lance.repOk(), "The lance's rep invariant should hold when it moves to the right!");
        }

        lance.setDirection(-1);
        for (int i=1; i<100; i++) {
            lance.updateXPosition(2);
            Assertions.assertTrue(lance.repOk(), "The lance's rep invariant should hold when it moves to the left!");
        }

    }

    @Test
    public void fireballMovementTest() {
        fireball.launchFireball();
        for (int i = 0; i <300; i++) {
            fireball.move();
            Assertions.assertTrue(fireball.repOk(), "The fireball's rep invariant should hold when it moves!");
        }
    }

    @Test
    public void barrierMovementTest() {
        for (int i = 0; i<300; i++) {
            simpleBarrier.move(1.5);
            Assertions.assertTrue(simpleBarrier.repOk(), "The simple barrier's rep invariant should hold when it moves horizontally!");
        }

        for (int i = 0; i <300; i++) {
            explosiveBarrier.move(1.5);
            Assertions.assertTrue(explosiveBarrier.repOk(), "The explosive barrier's rep invariant should hold when it moves circularly!");
        }
    }

    @Test
    public void collisionsTest() {
        simpleBarrier.setXPosition(4);
        fireball.launchFireball();

        for (int i = 0; i < 300; i++) {
            fireball.move();
            CollisionHandler.checkFireBallBorderCollisions(fireball, LanceOfDestiny.getScreenWidth(),LanceOfDestiny.getScreenHeight());
            CollisionHandler.checkFireBallEntityCollisions(fireball, simpleBarrier);
            Assertions.assertTrue(fireball.repOk(), "The fireball's rep invariant should hold even when it bounces from the barrier!");
        }
    }
}

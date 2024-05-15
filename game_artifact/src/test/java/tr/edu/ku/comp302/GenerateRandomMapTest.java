package tr.edu.ku.comp302;

import org.junit.jupiter.api.*;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.ExplosiveBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.BuildHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GenerateRandomMapTest {
    private MainFrame mainFrame;

    private BuildHandler buildHandler;

//TODO: ADD COMMENTS TO THE TESTS!!


    @BeforeEach
    public void setUp() {
        mainFrame = MainFrame.createMainFrame();
        buildHandler = ((BuildPanel) mainFrame.getBuildPanel()).getBuildHandler();
        mainFrame.showBuildPanel();
    }

    @Test
    public void mapNotNullTest() {
        Assertions.assertNotNull(buildHandler.generateRandomMapTest(75,10,10,10), "If more than 0 barriers were specified, the generated map cannot be null.");
    }

    @Test
    public void barrierCountTest() {
        ArrayList<Barrier> generatedMap = buildHandler.generateRandomMapTest(75,10,10,10);
        Assertions.assertEquals(105, generatedMap.size(), "The method should generate the correct number of barriers.");

        ArrayList<Barrier> otherGeneratedMap = buildHandler.generateRandomMapTest(100,20,20,20);
        Assertions.assertEquals(160, otherGeneratedMap.size(), "The method should generate the correct number of barriers.");

        ArrayList<Barrier> lastGeneratedMap = buildHandler.generateRandomMapTest(100,30,30,40);
        Assertions.assertEquals(200, lastGeneratedMap.size(), "The method should generate the correct number of barriers.");
    }

    @Test
    public void barrierDataTypeCountTest() {
        ArrayList<Barrier> generatedMap = buildHandler.generateRandomMapTest(75,10,10,10);
        int simpleCount = 0;
        int firmCount = 0;
        int explosiveCount = 0;
        int giftCount = 0;

        for (Barrier barrier: generatedMap) {
            if (barrier instanceof SimpleBarrier) {
                simpleCount++;
            }
            else if (barrier instanceof ExplosiveBarrier) {
                explosiveCount++;
            }
            else if (barrier instanceof FirmBarrier) {
                firmCount++;
            }

            /*
            else if (barrier instanceof GiftBarrier) {
                giftCount++;
            }
            */ //TODO: Uncomment this once we have gift barriers!

        }

        // 85 for now, since gift barriers are generated as simple barriers.
        Assertions.assertEquals(85, simpleCount, "The method should generate the correct number of simple barriers.");
        Assertions.assertEquals(10, firmCount, "The method should generate the correct number of firm barriers.");
        Assertions.assertEquals(10, explosiveCount, "The method should generate the correct number of explosive barriers.");
        // Assertions.assertEquals(10, giftCount, "The method should generate the correct number of gift barriers."); //TODO: Uncomment this once we have gift barriers.

    }

    @Test
    public void checkWindowBoundaries() {
        boolean windowBoundaryPassed = false;
        ArrayList<Barrier> generatedMap = buildHandler.generateRandomMapTest(100,30,30,40);
        int buildSectionWidth = ((BuildPanel)mainFrame.getBuildPanel()).getBuildSection().getWidth();
        int buildSectionHeight = ((BuildPanel)mainFrame.getBuildPanel()).getBuildSection().getHeight() * 4 / 8;
        for (Barrier barrier: generatedMap) {
            if (barrier.getYPosition() < 0 || barrier.getYPosition() > buildSectionHeight - 20|| barrier.getXPosition() < 0 || barrier.getXPosition() > buildSectionWidth - barrier.getLength() / 2) {
                windowBoundaryPassed = true;
                break;
            }
        }

        Assertions.assertFalse(windowBoundaryPassed, "The barrier positions should not pass the window boundaries!");

    }

    @Test
    public void checkCollisions() {
        boolean collisionDetected = false;
        ArrayList<Barrier> generatedMap = buildHandler.generateRandomMapTest(100,30,30,40);
        for (Barrier barrier: generatedMap) {
            if(buildHandler.checkBarrierCollisionWithBarriers(barrier)) {
                collisionDetected = true;
                break;
            }
        }

        Assertions.assertFalse(collisionDetected, "The barriers should not collide once a random map is generated.");
    }
}

package tr.edu.ku.comp302;

import org.junit.jupiter.api.*;
import tr.edu.ku.comp302.domain.entity.barrier.*;
import tr.edu.ku.comp302.domain.handler.BuildHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import tr.edu.ku.comp302.ui.panel.buildmode.BuildPanel;

import javax.swing.*;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class GenerateRandomMapTest {
    private MainFrame mainFrame;

    private BuildHandler buildHandler;



    // The set up creates a main frame, and opens the build panel. Since the visuals are
    // not needed for testing, the visibility is off.
    @BeforeEach
    public void setUp() {
        mainFrame = MainFrame.createMainFrame();
        buildHandler = ((BuildPanel) mainFrame.getBuildPanel()).getBuildHandler();
        mainFrame.showBuildPanel();
    }

    // A black box testing method to check whether or not the generated map is null.
    @Test
    public void mapNotNullTest() {
        Assertions.assertNotNull(buildHandler.generateRandomMapTest(75,10,10,10), "If more than 0 barriers were specified, the generated map cannot be null.");
    }

    // A black box testing method to check if the generated map contains the correct number of barriers. The edge cases
    // are when the barrier number is 100, and when the barrier number is 200. We can also check another
    // test case in the middle, which is 160 barriers.
    @Test
    public void barrierCountTest() {
        ArrayList<Barrier> generatedMap = buildHandler.generateRandomMapTest(75,10,5,10);
        Assertions.assertEquals(100, generatedMap.size(), "The method should generate the correct number of barriers.");

        ArrayList<Barrier> otherGeneratedMap = buildHandler.generateRandomMapTest(100,20,20,20);
        Assertions.assertEquals(160, otherGeneratedMap.size(), "The method should generate the correct number of barriers.");

        ArrayList<Barrier> lastGeneratedMap = buildHandler.generateRandomMapTest(100,30,30,40);
        Assertions.assertEquals(200, lastGeneratedMap.size(), "The method should generate the correct number of barriers.");
    }

    // A black box test to check if the generated number of barrier types are correct.
    // We thought that if a single test case works, all the rest would work as well,
    // since this process is very straightforward.
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

            else if (barrier instanceof GiftBarrier) {
                giftCount++;
            }

        }

        Assertions.assertEquals(75, simpleCount, "The method should generate the correct number of simple barriers.");
        Assertions.assertEquals(10, firmCount, "The method should generate the correct number of firm barriers.");
        Assertions.assertEquals(10, explosiveCount, "The method should generate the correct number of explosive barriers.");
        Assertions.assertEquals(10, giftCount, "The method should generate the correct number of gift barriers."); //TODO: Uncomment this once we have gift barriers.

    }

    // This test case creates 200 barriers, the most possible, and checks whether the barriers
    // pass the window boundaries or not. In the build mode, they should not be less than 0, and they should
    // not pass the build section's height/width with added paddings. Since this test case
    // creates the most barriers, if this test case works, all the others must work as well.
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

    // Creates the highest possible amount of barriers, 200, and checks if there are any collisions between
    // them. Since 200 barriers is the edge case and the highest case, if there are no collisions between
    // 200 barriers, there would be no collisions in any other case.
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

package tr.edu.ku.comp302;

import org.junit.Test;

import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.SpellHandler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class HexCollisionHandlerTest {

    /**
     * Test case to check if the spellHandler handles null inputs
     * properly and throws a NullPointerException
     * 
     * 
     * This test is towards glass-box testing since it expects a NullPointerException, 
     * which implies knowledge about the internal handling of null values.
     */
    @Test
    public void testNullInputs() {
        // Initialize the lists with null values
        List<Hex> hexes =null;
        List<Barrier> barriers = null;

        // Add some hexes and barriers for testing
        // (We are not adding any in this test case)

        // Create a new instance of SpellHandler
        SpellHandler spellHandler = new SpellHandler(null);
        
        // Add assertions to check the expected behavior after collision handling
        // Here, we are expecting the spellHandler to throw a NullPointerException
        // when handleHexCollision is called with null inputs

        // Use assertThrows to check if the expected exception is thrown
        // and to check if the thrown exception is of type NullPointerException
        assertThrows(NullPointerException.class, () -> spellHandler.handleHexCollision(hexes, barriers));
    }


    /**
     * Test case to check if a hex disappears when colliding with a barrier
     * 
     * This test ensures that all colliding hexes are handled correctly but does not inspect
     * the internal logic of the handleHexCollision method. Thus, it can be considered as a
     * blackbox test.
     */
    @Test
    public void testHexDisappearsWhenCollidingWithBarrier() {
        // Initialize a SpellHandler object
        SpellHandler spellHandler = new SpellHandler(null);
        
        // Create lists to hold the hexes and barriers
        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();

        // Create a hex and a barrier and add them to the respective lists
        Hex hexToBeCollided = new Hex(40, 40);
        Barrier barrier = new SimpleBarrier(40, 40);

        hexList.add(hexToBeCollided);
        barrierList.add(barrier);

        // Call the handleHexCollision method of the SpellHandler object
        // passing the lists as arguments
        spellHandler.handleHexCollision(hexList, barrierList);

        // Check if the hex list is empty, which indicates that the hex has disappeared
        // from the list
        assertTrue("Hex list should be empty after collision", hexList.isEmpty());
    }


    /**
     * Test case to check if the health of a barrier decreases when it collides with a hex
     * 
     * This test could be considered glass-box testing because it assumes that the barrier's health 
     * will be decremented by a specific value, which requires knowledge of how the collision affects
     * the barrier's health internally.
     */
    @Test
    public void testBarrierHealthDecreased() {
        // Initialize a SpellHandler object
        SpellHandler spellHandler = new SpellHandler(null);
        
        // Create lists to hold the hexes and barriers
        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();  

        // Create a hex and a barrier and add them to the respective lists
        Hex hex1 = new Hex(40, 40);
        hexList.add(hex1);
        Hex hex2 = new Hex(300, 300);
        hexList.add(hex2);

        FirmBarrier barrier1 = new FirmBarrier(40, 40);
        barrierList.add(barrier1);
        int barrierHealth = barrier1.getHealth();
        FirmBarrier barrier2 = new FirmBarrier(140, 140);
        barrierList.add(barrier2);

        // Call the handleHexCollision method of the SpellHandler object
        // passing the lists as arguments
        spellHandler.handleHexCollision(hexList, barrierList);
        // Check if the health of the barrier is decreased


        /**
         * Check if the health of the barrier is decreased by one
         * when a hex collides with it.
         */
        assertTrue(barrier1.getHealth() == barrierHealth - 1);

    } 



    /**
     * Test case to check if a hex disappears when colliding with a barrier
     * 
     * This test ensures that all colliding hexes are handled correctly but does not inspect the internal logic.
     * Thus, it can be considered as a blackbox test.
     */
    @Test
    public void testMultipleHexDisappearsWhenCollidingWithMultipleBarriers() {
        // Initialize a SpellHandler object
        SpellHandler spellHandler = new SpellHandler(null);
        
        // Create lists to hold the hexes and barriers
        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();

        // Create a hex and a barrier and add them to the respective lists
        for (int i = 0; i < 10; i++) {
            hexList.add(new Hex(i * 40, i));
            barrierList.add(new SimpleBarrier(i * 40, i));
        }

        

        // Call the handleHexCollision method of the SpellHandler object
        // passing the lists as arguments
        spellHandler.handleHexCollision(hexList, barrierList);

        // Check if the hex list is empty, which indicates that the hexes have disappeared
        // from the list
        assertTrue(hexList.isEmpty());
    }



    /**
     * Test case to check if a hex remains when it does not collide with a barrier.
     * 
     * In this test, a hex is created with a position that does not overlap with any of the barriers.
     * Then, the handleHexCollision method of the SpellHandler is called with the hex and the barriers.
     * Finally, the test checks if the hex is still in the list after the collision handling.
     * 
     * This test is towards blackbox testing since it does not inspect the internal logic of the method.
     */
    @Test
    public void testHexRemainsWhenNotCollidingWithBarrier() {
        SpellHandler spellHandler = new SpellHandler(null);
        
        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();

        // Create a hex positioned away from the barrier
        Hex hex = new Hex(50, 50); // Positioned away from the barrier
        Barrier barrier = new SimpleBarrier(100, 100);

        hexList.add(hex);
        barrierList.add(barrier);

        // Call the handleHexCollision method of the SpellHandler object
        spellHandler.handleHexCollision(hexList, barrierList);

        // Check if the hex is still in the list after the collision handling
        // This indicates that the hex did not collide with any of the barriers
        assertTrue(hexList.contains(hex));
    }



    /**
     * Test case to check if multiple hexes colliding with a single barrier are handled correctly.
     * 
     * This test ensures that all colliding hexes are handled correctly but does not inspect the internal logic.
     * Thus, it can be considered as a blackbox test.
     */
    @Test
    public void testMultipleHexesCollidingWithSingleBarrier() {
        SpellHandler spellHandler = new SpellHandler(null);
        
        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();

        // Create two hexes at the same position
        Hex hex1 = new Hex(40, 40);
        Hex hex2 = new Hex(40, 40);
        // Add them to the hex list
        hexList.add(hex1);
        hexList.add(hex2);
        
        // Create a barrier at the same position as the hexes
        Barrier barrier = new SimpleBarrier(40, 40);
        // Add it to the barrier list
        barrierList.add(barrier);
        
        // Call the handleHexCollision method of the SpellHandler object
        spellHandler.handleHexCollision(hexList, barrierList);

        // Check if both hexes are removed after collision
        // This indicates that the hexes have collided with the barrier
        assertTrue(hexList.isEmpty());
    }


    /**
     * Test case to check if only colliding hexes are removed.
     * 
     * In this test, a hex is created with a position that overlaps with a barrier and
     * another hex is created with a position that does not overlap with any of the barriers.
     * Then, the handleHexCollision method of the SpellHandler is called with the hex and the barriers.
     * Finally, the test checks if the colliding hex is removed from the list and the non-colliding
     * hex is still in the list after the collision handling.
     * 
     * This test is towards blackbox testing since it does not inspect the internal logic of the method.
     */
    @Test
    public void testPartialCollisions() {
        SpellHandler spellHandler = new SpellHandler(null);

        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();

        Hex hex1 = new Hex(40, 40); // Overlaps with the barrier
        Hex hex2 = new Hex(300, 300); // Does not overlap with any of the barriers
        Barrier barrier = new SimpleBarrier(40, 40);

        hexList.add(hex1);
        hexList.add(hex2);
        barrierList.add(barrier);

        spellHandler.handleHexCollision(hexList, barrierList);

        // Only the colliding hex should be removed
        assertFalse(hexList.contains(hex1)); // Hex1 overlaps with the barrier, so it should be removed
        assertTrue(hexList.contains(hex2)); // Hex2 does not overlap with any of the barriers, so it should stay
    }

}

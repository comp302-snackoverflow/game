package tr.edu.ku.comp302;

import org.junit.Test;

import tr.edu.ku.comp302.domain.entity.Hex;
import tr.edu.ku.comp302.domain.entity.barrier.Barrier;
import tr.edu.ku.comp302.domain.entity.barrier.FirmBarrier;
import tr.edu.ku.comp302.domain.entity.barrier.SimpleBarrier;
import tr.edu.ku.comp302.domain.handler.SpellHandler;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class HexCollisionHandlerTest {

    /**
     * Test case to check if the spellHandler handles null inputs
     * properly and throws a NullPointerException
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
        assertTrue(hexList.isEmpty());
    }


    //Test if the barrier health is decreased
    @Test
    public void testBarrierHealthDecreased() {
        // Initialize a SpellHandler object
        SpellHandler spellHandler = new SpellHandler(null);
        
        // Create lists to hold the hexes and barriers
        List<Hex> hexList = new ArrayList<>();
        List<Barrier> barrierList = new ArrayList<>();  

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


        assertTrue(barrier1.getHealth() == barrierHealth - 1);
    } 


    /**
     * Test case to check if a hex disappears when colliding with a barrier
     * 
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

}

package tr.edu.ku.comp302;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tr.edu.ku.comp302.domain.entity.Lance;


public class AdjustPositionAndSizeLanceTest {
    private Lance lance;
    private int oldWidth;
    private int oldHeight;

    // Reset screen size to default and initialize Lance object
    @Before
    public void setUp() {
        resetScreenSizeToDefault();
        lance = new Lance(500, 260);
    }

    // Black-box test: Tests if lance's size adjusts correctly when screen size increases
    @Test
    public void testLanceRelativeSizeWithIncreasedScreenSize() {
        int newWidth = 1920;
        int newHeight = 1080;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        // Expected length is newWidth / 10
        double expectedLength = newWidth / 10.0;
        // Thickness is constant (20) and should not change after resizing
        int expectedThickness = 20;

        // Assert the new length and thickness
        Assert.assertEquals(expectedLength, lance.getLength(), 0.001);
        Assert.assertEquals(expectedThickness, lance.getThickness(), 0.001);
    }

    // Black-box test: Tests if lance's position adjusts correctly when screen size increases
    @Test
    public void testLanceRelativePositionWithIncreasedScreenSize() {
        int newWidth = 1680;
        int newHeight = 1050;

        // Calculate expected positions
        double expectedXPosition = lance.getXPosition() * newWidth / oldWidth;
        double expectedYPosition = lance.getYPosition() * newHeight / oldHeight;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        // Assert the new x and y positions
        Assert.assertEquals(expectedXPosition, lance.getXPosition(), 0.001);
        Assert.assertEquals(expectedYPosition, lance.getYPosition(), 0.001);
    }

    // Glass-box test: Tests if lance's position is set to zero when screen size is zero
    @Test
    public void testLanceRelativePositionZeroScreenSize() {
        int newWidth = 0;
        int newHeight = 0;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        // Expected positions are zero
        double expectedXPosition = 0;
        double expectedYPosition = 0;

        // Assert the new x and y positions
        Assert.assertEquals(expectedXPosition, lance.getXPosition(), 0.001);
        Assert.assertEquals(expectedYPosition, lance.getYPosition(), 0.001);
    }

    // Glass-box test: Tests if lance's size is set to zero when screen size is zero
    @Test
    public void testLanceRelativeSizeZeroScreenSize() {
        int newWidth = 0;
        int newHeight = 0;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        // Expected length is zero
        double expectedLength = 0;
        // Thickness is constant (20)
        double expectedThickness = 20;

        // Assert the new length and thickness
        Assert.assertEquals(expectedLength, lance.getLength(), 0.001);
        Assert.assertEquals(expectedThickness, lance.getThickness(), 0.001);
    }

    // Black-box test: Tests if method throws an IllegalArgumentException when screen size is negative
    @Test
    public void testLanceRelativeSizeAndPositionWithNegativeScreenSize() {
        int newWidth = -1920;
        int newHeight = -1080;

        // Assert that IllegalArgumentException is thrown
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);
        });
    }

    // Helper method to reset screen size to default values
    private void resetScreenSizeToDefault() {
        oldWidth = 1280;
        oldHeight = 800;
    }
}
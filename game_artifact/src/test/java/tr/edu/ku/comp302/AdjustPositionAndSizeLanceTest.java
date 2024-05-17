package tr.edu.ku.comp302;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import tr.edu.ku.comp302.domain.entity.Lance;


public class AdjustPositionAndSizeLanceTest {
    private Lance lance;
    private int oldWidth;
    private int oldHeight;

    @Before
    public void setUp() {
        resetScreenSizeToDefault();
        lance = new Lance(500, 260);
    }

    @Test
    public void testLanceRelativeSizeWithIncreasedScreenSize() {
        int newWidth = 1920;
        int newHeight = 1080;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        double expectedLength = newWidth / 10.;
        int expectedThickness = 20; // Thickness is constant (20) and should not change after resizing
        Assert.assertEquals(expectedLength, lance.getLength(), 0.001);
        Assert.assertEquals(expectedThickness, lance.getThickness(), 0.001);
    }

    @Test
    public void testLanceRelativePositionWithIncreasedScreenSize() {

        int newWidth = 1680;
        int newHeight = 1050;


        double expectedXPosition = lance.getXPosition() * newWidth / oldWidth;
        double expectedYPosition = lance.getYPosition() * newHeight / oldHeight;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        Assert.assertEquals(expectedXPosition, lance.getXPosition(), 0.001);
        Assert.assertEquals(expectedYPosition, lance.getYPosition(), 0.001);
    }

    @Test
    public void testLanceRelativePositionZeroScreenSize(){
        int newWidth = 0;
        int newHeight = 0;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        double expectedXPosition = 0;
        double expectedYPosition = 0;

        Assert.assertEquals(expectedXPosition, lance.getXPosition(), 0.001);
        Assert.assertEquals(expectedYPosition, lance.getYPosition(), 0.001);
    }

    @Test
    public void testLanceRelativeSizeZeroScreenSize() {
        int newWidth = 0;
        int newHeight = 0;

        lance.adjustPositionAndSize(oldWidth, oldHeight, newWidth, newHeight);

        double expectedLength = 0;
        double expectedThickness = 20;

        Assert.assertEquals(expectedLength, lance.getLength(), 0.001);
        Assert.assertEquals(expectedThickness, lance.getThickness(), 0.001);
    }

    private void resetScreenSizeToDefault() {
        oldWidth = 1280;
        oldHeight = 800;
    }
}


package tr.edu.ku.comp302.chrono;

public class Chronometer {
    private final long[] arrowKeyPressTimes = new long[2];    // [rightArrowKeyPressTime, leftArrowKeyPressTime]
    private final double[] lanceMovementRemainder = new double[2];   // [remainderTotalRightArrowKey, remainderTotalLeftArrowKey]
    private long lastMovingTime;
    private boolean tapMoving;
    private Long pauseStartTime;
    private long previousTime;

    public Chronometer() {
        lastMovingTime = 0;
        tapMoving = false;
        pauseStartTime = null;
        previousTime = 0;
    }

    public void addPauseTime(long pauseDuration) {
        if (arrowKeyPressTimes[0] != 0){
            arrowKeyPressTimes[0] += pauseDuration;
        }
        if (arrowKeyPressTimes[1] != 0){
            arrowKeyPressTimes[1] += pauseDuration;
        }
        previousTime += pauseDuration;
        lastMovingTime += pauseDuration;
        pauseStartTime = null;
    }


    public long getCurrentTime(){
        return System.nanoTime();
    }

    public void resetArrowKeyPressTimes(){
        arrowKeyPressTimes[0] = 0L;
        arrowKeyPressTimes[1] = 0L;
    }

    public void resetLanceMovementRemainders(){
        lanceMovementRemainder[0] = 0L;
        lanceMovementRemainder[1] = 0L;
    }


    public long[] getArrowKeyPressTimes() {
        return arrowKeyPressTimes;
    }

    public long getArrowKeyPressTime(int index) {
        return arrowKeyPressTimes[index];
    }

    public void setArrowKeyPressTime(int index, long time) {
        arrowKeyPressTimes[index] = time;
    }

    public double getLanceMovementRemainder(int index) {
        return lanceMovementRemainder[index];
    }

    public void setLanceMovementRemainder(int index, double remaining) {
        lanceMovementRemainder[index] = remaining;
    }


    public double[] getLanceMovementRemainder() {
        return lanceMovementRemainder;
    }

    public long getLastMovingTime() {
        return lastMovingTime;
    }

    public void setLastMovingTime(long lastMovingTime) {
        this.lastMovingTime = lastMovingTime;
    }

    public boolean isTapMoving() {
        return tapMoving;
    }

    public void setTapMoving(boolean tapMoving) {
        this.tapMoving = tapMoving;
    }

    public Long getPauseStartTime() {
        return pauseStartTime;
    }

    public void setPauseStartTime(Long pauseStartTime) {
        this.pauseStartTime = pauseStartTime;
    }

    public long getPreviousTime() {
        return previousTime;
    }

    public void setPreviousTime(long previousTime) {
        this.previousTime = previousTime;
    }

    public static double nsToMs(long timeWithNs) {
        return timeWithNs / 1_000_000.0;
    }

    public static double nsToSeconds(long timeWithNs) {
        return timeWithNs / 1_000_000_000.0;
    }

}

package tr.edu.ku.comp302.domain.services.threads;


public class PausableThread{
    
    long startTime;
    long duration;
    long intervalTime;
    long nextRateTime;
    boolean paused;
    Runnable initialTask;
    Runnable finalTask = () -> {};
    private boolean isScheduled;

    /**
     * Creates a PausableThread with the given initialTask and finalTask.
     * 
     * The thread starts running immediately and runs for the given duration
     * (measured in milliseconds).
     * 
     * @param initialTask the initial task to run
     * @param finalTask the final task to run when the duration is reached
     * @param duration the duration of the thread in milliseconds
     */
    public PausableThread(Runnable initialTask, Runnable finalTask, long duration) {
        this.initialTask = initialTask;
        this.finalTask = finalTask;
        initialTask.run();
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        isScheduled = false;
    }

    /**
     * Creates a PausableThread with the given task.
     * 
     * The thread starts running immediately and runs at the given intervalTime
     * (measured in milliseconds). The task is run every intervalTime until
     * the thread is paused or the duration is reached.
     * 
     * @param task the task to run
     * @param duration the duration of the thread in milliseconds
     * @param intervalTime the time between each run of the task in milliseconds
     */
    public PausableThread(Runnable task, long duration, long intervalTime) {
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
        this.initialTask = task;
        this.intervalTime = intervalTime;
        this.nextRateTime = startTime + intervalTime;
        isScheduled = true;
    }


    public boolean checkFinishState(long currentTime){

        if((currentTime - startTime) >= duration){
            finalTask.run();
            return true;
        }


        return false;
    }

    public boolean checkRateState(long currentTime){    
        if(currentTime  >= nextRateTime){
            System.out.println("yes I am here");
            nextRateTime = currentTime + intervalTime;
            initialTask.run();
            return true;
        }
        return false;
    }


    public void pause(){
        paused = true;
        duration -= System.currentTimeMillis() - startTime;
    }

    public void resume(){
        if (paused == true){
            paused = false;
            startTime = System.currentTimeMillis();
            nextRateTime = startTime + intervalTime;
        }
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(long intervalTime) {
        this.intervalTime = intervalTime;
    }

    public long getNextRateTime() {
        return nextRateTime;
    }


    public boolean isScheduled() {
        return isScheduled;
    }

    
    

}


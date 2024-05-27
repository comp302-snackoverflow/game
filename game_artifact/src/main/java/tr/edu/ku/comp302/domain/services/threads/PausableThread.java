package tr.edu.ku.comp302.domain.services.threads;


public class PausableThread{
    
    long previousTime;
    long current;
    int duration;
    int passedTime = 0;
    int intervalTime;
    int nextRateTime;
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
     * @param duration the duration of the thread in seconds
     */
    public PausableThread(Runnable initialTask, Runnable finalTask, int duration) {
        this.initialTask = initialTask;
        this.finalTask = finalTask;
        initialTask.run();
        this.previousTime = System.currentTimeMillis();
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
     * @param duration the duration of the thread in seconds
     * @param intervalTime the time between each run of the task in seconds
     */
    public PausableThread(Runnable task, int duration, int intervalTime) {
        this.previousTime = System.currentTimeMillis();
        this.duration = duration;
        this.initialTask = task;
        this.intervalTime = intervalTime;
        nextRateTime = intervalTime;
        isScheduled = true;
    }


    public boolean checkFinishState(){

        updateTimes();

        if( passedTime >= duration){
            finalTask.run();
            return true;
        }


        return false;
    }

    public boolean checkRateState(){    


        if(passedTime  >= nextRateTime){
            System.out.println(passedTime + " " + nextRateTime  + " " + intervalTime);
            nextRateTime = passedTime + intervalTime;
            System.out.println(nextRateTime);
            initialTask.run();
            return true;
        }
        return false;
    }


    public void updateTimes(){
        current = System.currentTimeMillis();
        if (current - previousTime > 1000){
            passedTime++;
            previousTime = current;
        }
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPassedTime() {
        return passedTime;
    }

    public void setPassedTime(int passedTime) {
        this.passedTime = passedTime;
    }

    public boolean isScheduled() {
        return isScheduled;
    }

    public void setScheduled(boolean isScheduled) {
        this.isScheduled = isScheduled;
    }



    
    

}


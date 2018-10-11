package com.theopus.xengine.scheduler;

public abstract class SchedulerTask implements Runnable {

    private Scheduler.ThreadType threadType;
    private boolean repeatable;


    public SchedulerTask(Scheduler.ThreadType threadType, boolean repeatable) {
        this.threadType = threadType;
        this.repeatable = repeatable;
    }

    public Scheduler.ThreadType getThreadType() {
        return threadType;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

}

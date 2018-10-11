package com.theopus.xengine.scheduler;

public abstract class SchedulerTask implements Runnable {

    private long id;
    private Scheduler.ThreadType threadType;
    private boolean repeatable;
    private int perSecCap = 60;


    public SchedulerTask(Scheduler.ThreadType threadType, boolean repeatable) {
        this.threadType = threadType;
        this.repeatable = repeatable;
    }

    public SchedulerTask andThen(Callback callback){
        return new CallbackTask(this, callback);
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

    public int getPerSecCap() {
        return perSecCap;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setThreadType(Scheduler.ThreadType threadType) {
        this.threadType = threadType;
    }

    public void setPerSecCap(int perSecCap) {
        this.perSecCap = perSecCap;
    }
}

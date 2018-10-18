package com.theopus.xengine.nscheduler;

import com.google.common.util.concurrent.RateLimiter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Task implements Runnable, Comparable<Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

    private int id;
    private int priority;
    protected int executions;

    private Status status;
    private Context type;

    private Task onComplete;
    private Task onFinish;

    private final boolean cycled;

    private RateLimiter rateLimiter = RateLimiter.create(1);

    public Task(Context type, boolean cycled) {
        this.type = type;
        this.cycled = cycled;
        this.status = Status.NEW;
    }

    public int getId() {
        return id;
    }

    public int getExecutions() {
        return executions;
    }

    public Status getStatus() {
        return status;
    }

    void setStatus(Status status) {
        this.status = status;
    }

    public Task getOnComplete() {
        return onComplete;
    }

    public Task getOnFinish() {
        return onFinish;
    }

    public Context getType() {
        return type;
    }

    @Override
    public void run() {
        status = Status.RUNNING;
        process();
        executions++;
        status = Status.COMPLETED;
    }

    abstract public void process();

    public boolean throttle(){
        return rateLimiter.tryAcquire();
    }


    public boolean isCycled() {
        return cycled;
    }

    @Override
    public int compareTo(Task o) {
        return 0;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", priority=" + priority +
                ", executions=" + executions +
                ", status=" + status +
                ", type=" + type +
                ", onComplete=" + onComplete +
                ", onFinish=" + onFinish +
                ", cycled=" + cycled +
                ", rateLimiter=" + rateLimiter +
                '}';
    }
}

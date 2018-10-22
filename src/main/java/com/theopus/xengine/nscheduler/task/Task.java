package com.theopus.xengine.nscheduler.task;

import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Status;
import com.theopus.xengine.nscheduler.lock.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Task implements Runnable, Comparable<Task> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
    private static final AtomicInteger total = new AtomicInteger();

    private final int id;
    private final int priority;
    private final Context type;

    private int executions;
    private Status status;

    private Task onComplete;
    private Task onFinish;

    private long lastProcessTime;

    private final boolean cycled;

    private final RateLimiter rateLimiter;

    public Task() {
        this(Context.WORK, false);
    }

    public Task(Context type, boolean cycled) {
        this(type, cycled, 60);
    }

    public Task(Context type, boolean cycled, int rate) {
        this(type, cycled, rate, 10);
    }

    public Task(Context type, boolean cycled, int rate, int priority) {
        this.type = type;
        this.cycled = cycled;
        this.status = Status.NEW;
        this.rateLimiter = RateLimiter.create(rate);
        this.priority = priority;
        this.id = total.getAndIncrement();
    }

    public Task onCompete(Task onComplete) {
        this.onComplete = onComplete;
        return this;
    }

    public Task onFinish(Task onFinish) {
        this.onFinish = onFinish;
        return this;
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

    public void setStatus(Status status) {
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
        long start = System.currentTimeMillis();
        status = Status.RUNNING;
        try {
            preprocess();
            process();
            afterprocess();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executions++;
            status = Status.COMPLETED;
            lastProcessTime = System.currentTimeMillis() - start;
        }
    }

    abstract public void process() throws Exception;

    public void preprocess() {
    }

    public void afterprocess() {
    }

    public abstract boolean obtainLock(LockManager lockManager);

    public abstract void releaseLock(LockManager lockManager);

    public boolean throttle() {
        return rateLimiter.tryAcquire();
    }

    public boolean isCycled() {
        return cycled;
    }

    @Override
    public int compareTo(Task o) {
        return o.priority - this.priority;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", priority=" + priority +
                ", executions=" + executions +
                ", status=" + status +
                ", type=" + type +
                ", onComplete=" + (onComplete == null ? null : onComplete.id) +
                ", onFinish=" + (onFinish == null ? null : onFinish.id) +
                ", cycled=" + cycled +
                ", rateLimiter=" + rateLimiter +
                ", lastProcessTime=" + lastProcessTime +
                '}';
    }

    public long getLastProcessTime() {
        return lastProcessTime;
    }
}

package com.theopus.xengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.*;

public class Scheduler {

    public enum Type {
        MAIN, SIDE, WORK
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final ExecutorService mainExecutor;
    private final ExecutorService sideExecutor;
    private final ExecutorService workExecutor;

    private final List<Future<?>> running = new ArrayList();

    public Scheduler() {
        this.mainExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(11, new SchedulerTask.TaskComparator()));
        this.sideExecutor = new ThreadPoolExecutor(1, 1, 0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(11, new SchedulerTask.TaskComparator()));
        int workThreadsAvailable = Runtime.getRuntime().availableProcessors() - 2;
        this.workExecutor = new ThreadPoolExecutor(workThreadsAvailable <= 0 ? 1 : workThreadsAvailable, workThreadsAvailable <= 0 ? 1 : workThreadsAvailable, 0, TimeUnit.MILLISECONDS,
                new PriorityBlockingQueue<>(11, new SchedulerTask.TaskComparator()));
    }

    public void scheduleTask(SchedulerTask<?> task) {
        LOGGER.info("Scheduled task {}", task);

        task = task.isRepeatable() ? cycleTask(task) : task;

        switch (task.getType()){
            case MAIN: mainExecutor.submit(task); break;
            case SIDE: sideExecutor.submit(task); break;
            case WORK: workExecutor.submit(task); break;
            default: workExecutor.submit(task); break;
        }
    }

    public List<Future<?>> getRunning() {
        return running;
    }


    public void close() throws InterruptedException {
        mainExecutor.shutdownNow();
        sideExecutor.shutdownNow();
        workExecutor.shutdownNow();

        while (!(mainExecutor.isShutdown() || sideExecutor.isShutdown() || workExecutor.isShutdown())) {
            Thread.sleep(1);
        }
    }

    private SchedulerTask cycleTask(SchedulerTask task) {
        CallbackTask cycleWrapper = CallbackTask.of(task);
        return cycleWrapper.callback(() -> this.scheduleTask(cycleWrapper));
    }
}


abstract class SchedulerTask<V> implements Runnable, Comparable<SchedulerTask<V>> {

    private Scheduler.Type type;
    private int priority;
    private boolean repeatable;

    public SchedulerTask(Scheduler.Type type, int priority, boolean repeatable) {
        this.type = type;
        this.priority = priority;
        this.repeatable = repeatable;
    }

    public int getPriority() {
        return priority;
    }

    public Scheduler.Type getType() {
        return type;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    @Override
    public int compareTo(final SchedulerTask<V> o) {
        final long diff = o.priority - priority;
        return 0 == diff ? 0 : 0 > diff ? -1 : 1;
    }

    static class TaskComparator implements Comparator<Runnable> {
        @Override
        public int compare(Runnable left, Runnable right) {
            return ((SchedulerTask) left).compareTo((SchedulerTask) right);
        }
    }
}


class CallbackTask extends SchedulerTask {

    private Runnable task;
    private Callback callback;

    public CallbackTask(SchedulerTask task) {
        super(task.getType(), task.getPriority(), false);
        this.task = task;
    }

    public CallbackTask callback(Callback callback) {
        this.callback = callback;
        return this;
    }

    static CallbackTask of(SchedulerTask task) {
        return new CallbackTask(task);
    }

    @Override
    public void run() {
        task.run();
        callback.call();
    }
}


interface Callback {
    void call();
}





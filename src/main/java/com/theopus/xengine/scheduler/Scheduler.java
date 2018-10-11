package com.theopus.xengine.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Scheduler implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    public enum ThreadType {
        MAIN_CONTEXT, SIDE_CONTEXT, WORK, ANY
    }

    private ExecutorService mainContext = Executors.newSingleThreadExecutor();
    private ExecutorService sideContext = Executors.newSingleThreadExecutor();
    private ExecutorService work = Executors.newSingleThreadExecutor();

    private BlockingQueue<SchedulerTask> proposed = new LinkedBlockingQueue<>();
    private BlockingQueue<SchedulerTask> waiting = new LinkedBlockingQueue<>();

    public void propose(SchedulerTask task) {
        if (!accepting){

        }
        task = task.isRepeatable() ? wrapRepeatable(task) : task;
        proposed.offer(task);
    }

    public void operate() throws InterruptedException {
        SchedulerTask task = proposed.poll();
        if (task != null) {
            switch (task.getThreadType()) {
                case MAIN_CONTEXT:
                    mainContext.submit(task);
                    break;
                case SIDE_CONTEXT:
                    sideContext.submit(task);
                    break;
                case WORK:
                    work.submit(task);
                    break;
                case ANY:
                    work.submit(task);
                    break;
            }
        }
    }

    private SchedulerTask wrapRepeatable(SchedulerTask task) {
        task.setRepeatable(false);
        CallbackTask wrapTask = new CallbackTask(task);
        return wrapTask.andAfter(() -> {
            this.propose(wrapTask);
        });
    }

    @Override
    public void close() throws InterruptedException {
        mainContext.shutdown();
        sideContext.shutdown();
        work.shutdown();

        while (!(mainContext.isTerminated() && sideContext.isTerminated() && work.isTerminated())){
            Thread.sleep(10);
        }
    }
}

package com.theopus.xengine.nscheduler;

import org.apache.logging.log4j.core.util.ExecutorServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class ExecutorServiceFeeder implements Feeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceFeeder.class);


    private ExecutorService main = Executors.newSingleThreadExecutor();
    private ExecutorService side = Executors.newSingleThreadExecutor();
    private ExecutorService work = Executors.newFixedThreadPool(4);

    private Map<Integer, Future<?>> runningTasks = new ConcurrentSkipListMap<>();

    @Override
    public void feed(Task task) {
        LOGGER.info("Feeded: {} ", task.getId());

        task.setStatus(Status.SUBMITTED);
        Runnable wrapped = wrapRemoveFuture(task);
        switch (task.getType()) {
            case MAIN: {
                runningTasks.put(task.getId(),main.submit(wrapped));
            }
            break;
            case SIDE: {
                runningTasks.put(task.getId(),side.submit(wrapped));
            }
            break;
            case WORK: {
                runningTasks.put(task.getId(),work.submit(wrapped));
            }
            break;
            case ANY: {
                runningTasks.put(task.getId(),work.submit(wrapped));
            }
            break;
        }
    }

    @Override
    public void drain() throws ExecutionException, InterruptedException {
        for (Future<?> value : runningTasks.values()) {
            value.get();
        }
    }

    @Override
    public void close(){
        ExecutorServices.shutdown(main, 3, TimeUnit.SECONDS, "Feeder");
        ExecutorServices.shutdown(side, 3, TimeUnit.SECONDS, "Feeder");
        ExecutorServices.shutdown(work, 3, TimeUnit.SECONDS, "Feeder");
    }

    private Runnable wrapRemoveFuture(Task task){
        return () -> {
            task.run();
            runningTasks.remove(task.getId());
        };
    }
}

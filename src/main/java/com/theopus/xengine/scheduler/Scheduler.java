package com.theopus.xengine.scheduler;

import com.theopus.xengine.system.Configurer;
import com.theopus.xengine.trait.State;
import com.theopus.xengine.trait.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;

public class Scheduler implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    private boolean accepting = true;

    public Scheduler(StateManager stateManager) {
        this.manager = stateManager;
    }

    public void setAccepting(boolean accepting) {
        this.accepting = accepting;
    }

    public enum ThreadType {
        MAIN_CONTEXT, SIDE_CONTEXT, WORK, ANY
    }

    private StateManager manager;

    private ExecutorService mainContext = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private ExecutorService sideContext = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    private ExecutorService work = new ThreadPoolExecutor(1, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());

    private BlockingQueue<SchedulerTask> proposed = new LinkedBlockingQueue<>();
    private BlockingQueue<SchedulerTask> waiting = new LinkedBlockingQueue<>();
    private BlockingQueue<State> states = new LinkedBlockingQueue<>();

    private Map<Long, Future<?>> runningTasks = new ConcurrentSkipListMap<>();
    private long nextTaskId = 0;

    public void propose(SchedulerTask task) {
        if (!accepting) {
            return;
        }
        if (task.getId() == 0) {
            task.setId(++nextTaskId);
            task = wrapCleanFuture(task, task.getId());
        }
        task = task.isRepeatable() ? wrapRepeatable(task) : task;
        proposed.offer(task);
    }

    public void operate() throws InterruptedException {
        for (State state = states.poll(); state != null ; state = states.poll()) {
            manager.release(state);
        }

        SchedulerTask task = proposed.poll();
        if (task != null) {

            Configurer configurer = task.getSystem().configurer();
            switch (configurer.type()) {
                case READ_ONLY:
                    State onlyRead = manager.forRead();
                    task = wrapFreeStatePut(task, onlyRead);
                    configurer.setRead(onlyRead);
                    break;
                case READ_WRITE:
                    State write = manager.forWrite();
                    task = wrapFreeStatePut(task, write);
                    State read = manager.forRead();
                    task = wrapFreeStatePut(task, read);

                    configurer.setRead(read);
                    configurer.setWrite(write);
                    break;
                default:
                    proposed.add(task);
                    return;
            }

            switch (task.getThreadType()) {
                case MAIN_CONTEXT:
                    runningTasks.put(task.getId(), mainContext.submit(task));
                    break;
                case SIDE_CONTEXT:
                    runningTasks.put(task.getId(), sideContext.submit(task));
                    break;
                case WORK:
                    runningTasks.put(task.getId(), work.submit(task));
                    break;
                case ANY:
                    runningTasks.put(task.getId(), work.submit(task));
                    break;
            }
        }

//        Thread.sleep(1000);
    }

    public void drain() {
        LOGGER.info("Drain initiated.");
        accepting = false;
        runningTasks.values().forEach(future -> {
            try {
                future.get();
                LOGGER.info("Task to be finished {}", runningTasks.size());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }

    private SchedulerTask wrapFreeStatePut(SchedulerTask task, State state){
        return task.andThen(() -> this.states.add(state));
    }

    private SchedulerTask wrapRepeatable(SchedulerTask task) {
        task.setRepeatable(false);
        final CallbackTask callbackWrap = new CallbackTask(task);
        return callbackWrap.withCallback(() -> this.propose(callbackWrap));
    }

    private SchedulerTask wrapCleanFuture(SchedulerTask task, long id) {
        CallbackTask callbackWrap = new CallbackTask(task);
        return callbackWrap.withCallback(() -> this.runningTasks.remove(id));
    }

    @Override
    public void close() throws InterruptedException {
        mainContext.shutdown();
        sideContext.shutdown();
        work.shutdown();

        while (!(mainContext.isTerminated() && sideContext.isTerminated() && work.isTerminated())) {
            Thread.sleep(10);
        }
    }
}

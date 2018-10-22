package com.theopus.xengine.nscheduler;

import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.task.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

public class Scheduler<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final LockManager lockManager;
    private final Feeder feeder;
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    private final Queue<Task> finished = new LinkedList<>();

    private boolean cycle;

    public Scheduler(LockManager<T> lockManager, Feeder feeder) {
        this.lockManager = lockManager;
        this.feeder = feeder;
        this.cycle = true;
    }

    public void propose(Task task) {
        if (task == null) {
            return;
        }
        queue.add(task);
    }

    public void process() {
        for (Iterator<Task> iterator = queue.iterator(); iterator.hasNext(); ) {
            Task task = iterator.next();
            switch (task.getStatus()) {
                case NEW:
                    if (task.throttle()) {
                        boolean success = task.obtainLock(lockManager);
                        if (success) {
                            feeder.feed(task);
                        } else {
                            task.releaseLock(lockManager);
                        }
                    }
                    break;

                case COMPLETED:
                    task.releaseLock(lockManager);
                    if (cycle && task.isCycled()) {
                        task.setStatus(Status.NEW);
                    } else {
                        task.setStatus(Status.FINISHED);
                        propose(task.getOnComplete());
                    }
                    break;

                case FINISHED:
                    iterator.remove();
                    finished.add(task);
                    propose(task.getOnFinish());
                    break;
            }
        }
    }

    public void drain() throws ExecutionException, InterruptedException {
        this.cycle = false;
        feeder.drain();
        while (!queue.isEmpty()) {
            process();
        }
        feeder.drain();
        LOGGER.info("After drain queue: {}", queue);
    }

    public void close() throws ExecutionException, InterruptedException {
        drain();
        feeder.close();

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scheduler<String> scheduler = new Scheduler<>(new LockManager<>(id -> new Lock<>(id, "Test"), 3), new ExecutorServiceFeeder());

        Task task = TaskChain.startWith(new ReadWriteTask(Context.MAIN, false) {
            @Override
            public void process() {
                LOGGER.info("Prepare main 0 cycle {}", getExecutions());
            }
        }).andThen(new ReadWriteTask(Context.MAIN, false) {
            @Override
            public void process() {
                LOGGER.info("Prepare main 1 cycle {}", getExecutions());
            }
        }).onFinish(new ReadWriteTask(Context.WORK, true, 1) {
            @Override
            public void process() {
                LOGGER.info("Update cycle {} took {}", getExecutions(), getLastProcessTime());
            }
        }).andThen(new ReadTask(Context.MAIN, true, 2) {
            @Override
            public void process() {
                LOGGER.info("Run 1 cycle main {} took", getExecutions(), getLastProcessTime());
            }
        }).andThen(new ReadWriteTask(Context.MAIN, false) {
            @Override
            public void process() {
                LOGGER.info("Teardown cycle main {}", getExecutions());
            }
        }).head();

        scheduler.propose(task);

        for (int i = 0; i < 10_000_0_000; i++) {
            scheduler.process();
        }
        scheduler.close();
//        LOGGER.info("Finished list{}", scheduler.getFinished().stream().map(Task::toString).collect(Collectors.joining("\n")));
    }

    public Queue<Task> getFinished(){
        return finished;
    }
}

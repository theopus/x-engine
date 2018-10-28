package com.theopus.xengine.nscheduler;

import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.nscheduler.task.Feeder;
import com.theopus.xengine.nscheduler.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

public class Scheduler implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private final Feeder feeder;
    private final PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    private final Queue<Task> finished = new LinkedList<>();

    private boolean cycle;
    private long tick = 0L;

    public Scheduler(Feeder feeder) {
        this.feeder = feeder;
        this.cycle = true;
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());

        Task processed = new Task(Context.INLINE, true, Integer.MAX_VALUE) {
            @Override
            public void process() throws Exception {
                LOGGER.info("Processed");
            }

            @Override
            public void injectManagers(EventManager em, InputManager im, LockManager lm) {

            }
        };
        scheduler.propose(processed);

        for (int i = 0; i < 10_000_0_000; i++) {
            System.out.println(processed);
            System.out.println(scheduler.tick);
            scheduler.process();
        }
        scheduler.close();
//        LOGGER.info("Finished list{}", scheduler.getFinished().stream().map(Task::toString).collect(Collectors.joining("\n")));
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
                    handleNew(task);
                    break;
                case COMPLETED:
                    handleComplete(task);
                    break;
                case FINISHED:
                    handleFinish(iterator, task);
                    break;
            }
        }
        tick++;
    }

    private void handleNew(Task task) {
        if (task.shouldThrottle()) {
            boolean success = task.prepare();
            if (success) {
                feeder.feed(task);
            } else {
                task.rollback();
            }
        }
    }

    private void handleComplete(Task task) {
        boolean result = task.finish();
        if (cycle && task.isCycled()) {
            task.setStatus(Status.NEW);
        } else {
            task.setStatus(Status.FINISHED);
            propose(task.getOnComplete());
        }
    }

    private void handleFinish(Iterator<Task> iterator, Task task) {
        iterator.remove();
        finished.add(task);
        propose(task.getOnFinish());
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

    @Override
    public void close() throws ExecutionException, InterruptedException {
        LOGGER.info("Closing...");
        drain();
        feeder.close();

    }

    public Queue<Task> getFinished() {
        return finished;
    }


}

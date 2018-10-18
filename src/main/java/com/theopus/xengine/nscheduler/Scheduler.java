package com.theopus.xengine.nscheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.PriorityBlockingQueue;

public class Scheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

    private PriorityBlockingQueue<Task> queue = new PriorityBlockingQueue<>();
    private Queue<Task> finished = new LinkedList<>();
    private Feeder feeder;
    private boolean cycle;

    public Scheduler(Feeder feeder) {
        this.feeder = feeder;
        this.cycle = true;
    }

    public void propose(Task task) {
        if (task == null){
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
                        feeder.feed(task);
                    }
                    break;

                case COMPLETED:
                    if (cycle && task.isCycled()){
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

    private void drain() throws ExecutionException, InterruptedException {
        this.cycle = false;
        feeder.drain();
        while (!queue.isEmpty()){
            process();
        }
        feeder.drain();
        LOGGER.info("After drain queue: {}", queue);
    }

    private void close() throws ExecutionException, InterruptedException {
        drain();
        feeder.close();

    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());
        Task task = new Task(Context.MAIN, true ){

            private final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);

            @Override
            public void process() {
                LOGGER.info("Process cycle {}", executions);
            }
        };
        scheduler.propose(task);

        for (int i = 0; i < 100_000_000; i++) {
            scheduler.process();
        }

        System.out.println(task.getExecutions());
        scheduler.close();


    }
}

package com.theopus.xengine.nscheduler.event;

import com.google.common.collect.ImmutableMap;
import com.theopus.xengine.conc.EventReadTask;
import com.theopus.xengine.conc.EventWriteTask;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.nscheduler.task.TaskFactory;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventManagerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EventManagerTest.class);

    @Test
    public void name() throws InterruptedException {
        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());
        EventManager eventManager = new EventManager(ImmutableMap.of(
                EventManager.Topics.INPUT_DATA_TOPIC.id, EventManager.Topics.INPUT_DATA_TOPIC
        ));
        TaskFactory factory = new TaskFactory(
                null,
                eventManager,
                null);

        EventReadTask readTask = new EventReadTask(Context.MAIN, true) {
            @Override
            public void process() throws Exception {
                reader.read().forEach(System.out::println);
            }
        };

        EventWriteTask writeTask = new EventWriteTask(Context.WORK, true) {
            @Override
            public void process() throws Exception {
                writer.write(new Event<>(new InputData(0, 1)));
            }
        };

        scheduler.propose(factory.injectManagers(readTask));
        scheduler.propose(factory.injectManagers(writeTask));

        for (int i = 0; i < 10_000_000; i++) {
            Thread.sleep(1000);
            eventManager.logTopics();
            eventManager.trimTo(5);
            scheduler.process();
        }
    }
}
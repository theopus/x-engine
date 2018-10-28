package com.theopus.xengine.nscheduler.task;

import com.google.common.collect.ImmutableMap;
import com.theopus.xengine.conc.State;
import com.theopus.xengine.conc.StateFactory;
import com.theopus.xengine.inject.Entity;
import com.theopus.xengine.inject.Event;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.event.TopicWriter;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.input.InputReader;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.lock.LockUser;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.custom.RenderTraitEditor;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TaskTest {

    @Test
    public void name() throws NoSuchFieldException, IllegalAccessException {

        LockManager<State> lm = new LockManager<State>(new StateFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        )), 3);

        EventManager em = new EventManager(ImmutableMap.of(
                EventManager.Topics.INPUT_DATA_TOPIC.getId(), EventManager.Topics.INPUT_DATA_TOPIC
        ));

        TaskFactory<State> factory = new TaskFactory<>(lm, null, null);


        TestTask testTask = new TestTask();

        inj(lm, em, testTask);

        testTask.prepare();
        testTask.process();

    }

    private void inj(LockManager<State> lm, EventManager em, ComponentTask task) throws NoSuchFieldException, IllegalAccessException {

        Class<? extends ComponentTask> tc = task.getClass();
        System.out.println(Arrays.toString(ComponentTask.class.getDeclaredFields()));
        Field components = ComponentTask.class.getDeclaredField("components");
        components.setAccessible(true);

        List<TaskComponent> list = (List<TaskComponent>) components.get(task);
        list.clear();

        for (Field field : tc.getDeclaredFields()) {
            System.out.println(field);
            boolean entity = field.isAnnotationPresent(Entity.class);
            if (entity) {
                field.setAccessible(true);
                Entity annotation = field.getAnnotation(Entity.class);
                LockUser<State> lock;
                if (annotation.value() == Lock.Type.WRITE_READ) {
                    lock = lm.createReadWrite();
                } else {
                    lock = lm.createReadOnly();
                }
                list.add(lock);
                field.set(task, lock);
            }
            boolean event = field.isAnnotationPresent(Event.class);
            if (event) {
                field.setAccessible(true);

                Event annotation = field.getAnnotation(Event.class);
                if (annotation.type() == Event.READ) {
                    TopicReader<?> reader = em.createReader(annotation.topicId());
                    list.add(reader);
                    field.set(task, reader);
                } else if (annotation.type() == Event.WRITE) {
                    TopicWriter<?> writer = em.createWriter(annotation.topicId());
                    list.add(writer);
                    field.set(task, writer);
                }
            }
        }
    }


    static class TestTask extends ComponentTask {

        @Entity(Lock.Type.WRITE_READ)
        private LockUser<State> lockUser;

        @Event(topicId = 0, type = Event.READ)
        private TopicReader<InputReader> reader;

        @Event(topicId = 0, type = Event.WRITE)
        private TopicWriter<InputReader> writer;

        @Override
        public void process() {
        }

        @Override
        public void injectManagers(EventManager em, InputManager im, LockManager lm) {

        }
    }
}
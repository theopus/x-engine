package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.inject.Entity;
import com.theopus.xengine.inject.Event;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.event.TopicWriter;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.lock.LockUser;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class TaskConfigurer {

    private final LockManager<?> lockManager;
    private final EventManager eventManager;
    private final InputManager inputManager;

    public TaskConfigurer(LockManager<?> lockManager, EventManager eventManager, InputManager inputManager) {
        this.lockManager = lockManager;
        this.eventManager = eventManager;
        this.inputManager = inputManager;
    }


    public <T extends Task> T injectManagers(T task) {
        if (task == null) {
            return task;
        }
        task.injectManagers(eventManager, inputManager, lockManager);
        return task;
    }

    public TaskChain injectManagers(TaskChain chain) {
        for (ComponentTask t = (ComponentTask) chain.head(); t != null; t = (ComponentTask) t.getOnComplete()) {
            try {
                inj(t);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            injectManagers(t);
            injectManagers(t.getOnFinish());
        }
        return chain;
    }

    private void inj(ComponentTask task) throws NoSuchFieldException, IllegalAccessException {

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
                LockUser lock;
                if (annotation.value() == Lock.Type.WRITE_READ) {
                    lock = lockManager.createReadWrite();
                } else {
                    lock = lockManager.createReadOnly();
                }
                list.add(lock);
                field.set(task, lock);
            }
            boolean event = field.isAnnotationPresent(Event.class);
            if (event) {
                field.setAccessible(true);

                Event annotation = field.getAnnotation(Event.class);
                if (annotation.type() == Event.READ) {
                    TopicReader<?> reader = eventManager.createReader(annotation.topicId());
                    list.add(reader);
                    field.set(task, reader);
                } else if (annotation.type() == Event.WRITE) {
                    TopicWriter<?> writer = eventManager.createWriter(annotation.topicId());
                    list.add(writer);
                    field.set(task, writer);
                }
            }
        }
    }
}

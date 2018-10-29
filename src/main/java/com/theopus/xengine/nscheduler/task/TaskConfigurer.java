package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.inject.InjectEvent;
import com.theopus.xengine.inject.InjectLock;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.event.TopicWriter;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.lock.LockUser;

import java.lang.reflect.Field;
import java.util.ArrayList;
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
        for (Task t = chain.head(); t != null; t = t.getOnComplete()) {
            inject(t);
            inject(t.getOnFinish());
        }
        return chain;
    }

    private void inject(Task t) {
        if (t instanceof ComponentTask) {
            try {
                inj((ComponentTask) t);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            injectManagers(t);
        } else {
            injectManagers(t);
        }
    }

    private void inj(ComponentTask task) throws NoSuchFieldException, IllegalAccessException {
        Class<? extends ComponentTask> tc = task.getClass();
        Field components = ComponentTask.class.getDeclaredField("components");
        components.setAccessible(true);

        List<TaskComponent> list = (List<TaskComponent>) components.get(task);
        list.clear();

        List<Field> fields = collectFields(tc);

        for (Field field : fields) {
            System.out.println(field);
            boolean entity = field.isAnnotationPresent(InjectLock.class);
            if (entity) {
                field.setAccessible(true);
                InjectLock annotation = field.getAnnotation(InjectLock.class);
                LockUser lock;
                System.out.println(field);
                if (annotation.value() == Lock.Type.WRITE_READ) {
                    lock = lockManager.createReadWrite();
                    System.out.println("write");
                } else {
                    lock = lockManager.createReadOnly();
                    System.out.println("read");
                }

                list.add(lock);
                field.set(task, lock);
            }

            boolean event = field.isAnnotationPresent(InjectEvent.class);
            if (event) {
                field.setAccessible(true);
                InjectEvent annotation = field.getAnnotation(InjectEvent.class);
                if (annotation.type() == InjectEvent.READ) {
                    TopicReader<?> reader = eventManager.createReader(annotation.topicId());
                    list.add(reader);
                    field.set(task, reader);
                } else if (annotation.type() == InjectEvent.WRITE) {
                    TopicWriter<?> writer = eventManager.createWriter(annotation.topicId());
                    list.add(writer);
                    field.set(task, writer);
                }
            }
        }
    }

    private List<Field> collectFields(Class<? extends ComponentTask> tc) {
        List<Field> result = new ArrayList<>();
        for (Class cl = tc; cl != null ; cl = cl.getSuperclass()) {
            result.addAll(Arrays.asList(cl.getDeclaredFields()));
        }
        return result;
    }
}

package com.theopus.xengine.event;

import com.theopus.xengine.inject.Event;
import com.theopus.xengine.inject.Provider;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.lang.reflect.Field;
import java.util.List;

public class EventProvider implements Provider {

    private EventManager manager;

    public EventProvider(EventManager manager) {
        this.manager = manager;
    }

    public void provide(Object target, List<TaskComponent> container, Task task) throws IllegalAccessException {

        Class<?> targetClass = target.getClass();

        List<Field> fields = collectFields(targetClass);
//        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            boolean event = field.isAnnotationPresent(Event.class);
            if (event) {
                field.setAccessible(true);
                Event annotation = field.getAnnotation(Event.class);
                if (field.getType().equals(TopicReader.class)) {
                    boolean listener = annotation.listener();
                    TopicReader<?> reader = manager.createReader(annotation.topicId());
                    container.add(reader);
                    field.set(target, reader);

                    if (listener) {
                        manager.listen(task, reader);
                    }
                } else if (field.getType().equals(TopicWriter.class)) {
                    TopicWriter<?> writer = manager.createWriter(annotation.topicId());
                    container.add(writer);
                    field.set(target, writer);
                }
            }
        }
    }
}

package com.theopus.xengine.event;

import com.theopus.xengine.inject.Event;
import com.theopus.xengine.inject.Provider;
import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.lang.reflect.Field;
import java.util.List;

public class EventProvider implements Provider {

    private EventManager manager;

    public EventProvider(EventManager manager) {
        this.manager = manager;
    }

    public void provide(Object target, List<TaskComponent> container) throws IllegalAccessException {

        Class<?> targetClass = target.getClass();

        List<Field> fields = collectFields(targetClass);
//        Field[] fields = targetClass.getDeclaredFields();

        for (Field field : fields) {
            boolean event = field.isAnnotationPresent(Event.class);
            if (event) {
                field.setAccessible(true);
                Event annotation = field.getAnnotation(Event.class);
                if (field.getType().equals(TopicReader.class)) {
                    TopicReader<?> reader = manager.createReader(annotation.topicId());
                    container.add(reader);
                    field.set(target, reader);
                } else if (field.getType().equals(TopicWriter.class)) {
                    TopicWriter<?> writer = manager.createWriter(annotation.topicId());
                    container.add(writer);
                    field.set(target, writer);
                }
            }
        }
    }
}

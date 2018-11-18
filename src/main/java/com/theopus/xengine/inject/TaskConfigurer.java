package com.theopus.xengine.inject;

import com.theopus.xengine.ecs.EcsProvider;
import com.theopus.xengine.event.EventProvider;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.lang.reflect.Field;
import java.util.List;

public class TaskConfigurer {

    private EcsProvider ecsProvider;
    private EventProvider eventProvider;

    public TaskConfigurer(EcsProvider ecsProvider, EventProvider eventProvider) {
        this.ecsProvider = ecsProvider;
        this.eventProvider = eventProvider;
    }

    public void configure(Task task){
        configure(task, task);
    }

    public void configure(Object target, Task task) {
        try {
            Field components = ComponentTask.class.getDeclaredField("components");

            components.setAccessible(true);

            List<TaskComponent> container = (List<TaskComponent>) components.get(task);

            ecsProvider.provide(target, container, task);
            eventProvider.provide(target, container, task);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }


}

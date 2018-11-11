package com.theopus.xengine.ecs.system;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.SystemTask;
import com.theopus.xengine.nscheduler.task.Task;

public abstract class TaskSystem implements BaseSystem {

    private final SystemTask task;

    public TaskSystem(Context context, boolean repetable, float rate) {
        task = new SystemTask(context, repetable, rate, this);
    }

    @Override
    public Task task(){
        return task;
    }
}

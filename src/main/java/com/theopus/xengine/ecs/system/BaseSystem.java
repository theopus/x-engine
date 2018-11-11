package com.theopus.xengine.ecs.system;

import com.theopus.xengine.nscheduler.task.Task;

public interface BaseSystem {

    void process();

    void init();

    Task task();

}

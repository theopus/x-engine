package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.ecs.system.BaseSystem;
import com.theopus.xengine.nscheduler.Context;

public class SystemTask extends ComponentTask {
    private final BaseSystem system;

    public SystemTask(BaseSystem system) {
        this.system = system;
    }

    public SystemTask(Context type, boolean cycled, BaseSystem system) {
        super(type, cycled);
        this.system = system;
    }

    public SystemTask(Context type, boolean cycled, float rate, BaseSystem system) {
        super(type, cycled, rate);
        this.system = system;
    }

    public SystemTask(Context type, boolean cycled, float rate, int priority, BaseSystem system) {
        super(type, cycled, rate, priority);
        this.system = system;
    }

    @Override
    public void process() throws Exception {
        system.process();
    }
}

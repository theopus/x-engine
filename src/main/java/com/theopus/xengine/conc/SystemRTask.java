package com.theopus.xengine.conc;

import com.theopus.xengine.inject.InjectLock;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockUser;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemRTask extends ComponentTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRTask.class);

    protected final System system;
    @InjectLock(Lock.Type.READ)
    protected LockUser<State> lock;

    public SystemRTask(System system) {
        this.system = system;
    }

    public SystemRTask(Context type, boolean cycled, System system) {
        super(type, cycled);
        this.system = system;
    }

    public SystemRTask(Context type, boolean cycled, int rate, System system) {
        super(type, cycled, rate);
        this.system = system;
    }

    public SystemRTask(Context type, boolean cycled, int rate, int priority, System system) {
        super(type, cycled, rate, priority);
        this.system = system;
    }

    @Override
    public void preprocess() {
        lock.log();
        system.configurer().setRead(lock.readContent());
    }

    @Override
    public void process() {
        system.process();
    }
}

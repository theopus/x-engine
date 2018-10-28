package com.theopus.xengine.conc;

import com.theopus.xengine.inject.Entity;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockUser;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemRWTask extends ComponentTask {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRWTask.class);

    protected final System system;

    @Entity(Lock.Type.WRITE_READ)
    protected LockUser<State> lock;

    public SystemRWTask(System system) {
        this.system = system;
    }

    public SystemRWTask(Context type, boolean cycled, System system) {
        super(type, cycled);
        this.system = system;
    }

    public SystemRWTask(Context type, boolean cycled, int rate, System system) {
        super(type, cycled, rate);
        this.system = system;
    }

    public SystemRWTask(Context type, boolean cycled, int rate, int priority, System system) {
        super(type, cycled, rate, priority);
        this.system = system;
    }

    @Override
    public void preprocess() {
        lock.log();
        system.configurer().setWrite(lock.readContent(), lock.writeContent());
    }

    @Override
    public void process() throws Exception {
        system.process();
    }
}

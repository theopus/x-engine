package com.theopus.xengine.conc;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.ReadWriteTask;
import com.theopus.xengine.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemRWTask extends ReadWriteTask<State> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemRWTask.class);

    protected final System system;

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
        LOGGER.debug("RW {}->{} base on {}",writeLock.getFrame(), writeLock.getNextFrame(), readLock.getFrame());
        system.configurer().setWrite(readLock.getOf(), writeLock.getOf());
    }

    @Override
    public void process() throws Exception {
        system.process();
    }
}

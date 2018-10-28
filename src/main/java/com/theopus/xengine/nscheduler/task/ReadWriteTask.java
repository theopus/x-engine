package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadWriteTask<T> extends ReadTask<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteTask.class);

    public ReadWriteTask() {
    }

    public ReadWriteTask(Context type, boolean cycled) {
        super(type, cycled);
    }

    public ReadWriteTask(Context type, boolean cycled, int rate) {
        super(type, cycled, rate);
    }

    public ReadWriteTask(Context type, boolean cycled, int rate, int priority) {
        super(type, cycled, rate, priority);
    }

    @Override
    public void injectManagers(EventManager em, InputManager im, LockManager lm) {
        LOGGER.debug("Injected to {}", this);
        lock = lm.createReadWrite();
    }
}

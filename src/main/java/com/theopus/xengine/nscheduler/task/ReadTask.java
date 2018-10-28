package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.lock.LockUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadTask<T> extends Task {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadTask.class);
    protected LockUser<T> lock;

    public ReadTask() {
    }

    public ReadTask(Context type, boolean cycled) {
        super(type, cycled);
    }

    public ReadTask(Context type, boolean cycled, int rate) {
        super(type, cycled, rate);
    }

    public ReadTask(Context type, boolean cycled, int rate, int priority) {
        super(type, cycled, rate, priority);
    }

    @Override
    public boolean prepare() {
        return lock.prepare();
    }

    @Override
    public boolean rollback() {
        return lock.rollback();
    }

    @Override
    public boolean finish() {
        return lock.finish();
    }

    @Override
    public void injectManagers(EventManager em, InputManager im, LockManager lm) {
        LOGGER.debug("Injected to {}", this);
        this.lock = lm.createReadOnly();
    }
}

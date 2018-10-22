package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;

public abstract class ReadTask<T> extends Task {
    protected Lock<T> readLock;

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
    public boolean obtainLock(LockManager lockManager) {
        readLock = lockManager.forRead();
        return readLock != null;
    }

    @Override
    public void releaseLock(LockManager lockManager) {
        lockManager.release(readLock);
    }
}

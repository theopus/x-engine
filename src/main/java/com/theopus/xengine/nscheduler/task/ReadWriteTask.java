package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ReadWriteTask<T> extends ReadTask<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadWriteTask.class);

    protected Lock<T> writeLock;

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
    public boolean obtainLock(LockManager lockManager) {
        boolean readStatus = super.obtainLock(lockManager);
        writeLock = lockManager.forWrite();
        return readStatus && writeLock != null;
    }

    @Override
    public void releaseLock(LockManager lockManager) {
        super.releaseLock(lockManager);
        lockManager.release(writeLock);
    }
}

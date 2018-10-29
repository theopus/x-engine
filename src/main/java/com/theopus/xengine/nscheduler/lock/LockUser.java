package com.theopus.xengine.nscheduler.lock;

import com.theopus.xengine.nscheduler.task.TaskComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockUser<T> implements TaskComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockUser.class);
    private final LockManager<T> lock;
    private Lock<T> read;
    private Lock<T> write;
    private boolean onlyRead;

    public LockUser(LockManager<T> lock, boolean onlyRead) {
        this.lock = lock;
        this.onlyRead = onlyRead;
    }

    public boolean prepare() {
        LOGGER.debug("Prepare...");
        if (onlyRead) {
            this.read = lock.forRead();
            return read != null;
        } else {
            this.read = lock.forRead();
            this.write = lock.forWrite();
            return read != null && write != null;
        }
    }

    public boolean rollback() {
        this.finish();
        return true;
    }

    public boolean finish() {
        LOGGER.debug("Finish...");
        lock.release(read);
        lock.release(write);
        return true;
    }

    public T readContent() {
        return read.getOf();
    }

    public T writeContent() {
        return write.getOf();
    }

    public void log() {
        LOGGER.debug("LockUser id: readLock: {}, writeLock: {}", read, write);
    }
}

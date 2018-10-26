package com.theopus.xengine.nscheduler.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LockUser<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockUser.class);

    private Lock<T> read;
    private Lock<T> write;
    private final LockManager<T> lock;
    private boolean onlyRead;

    public LockUser(LockManager<T> lock, boolean onlyRead) {
        this.lock = lock;
        this.onlyRead = onlyRead;
    }

    public boolean prepare() {
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

    public void log(){
        LOGGER.debug("LockUser id: readLock: {}, writeLock: {}", read, write);
    }
}

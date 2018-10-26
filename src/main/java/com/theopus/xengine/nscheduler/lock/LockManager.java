package com.theopus.xengine.nscheduler.lock;

import com.theopus.xengine.utils.UpdatableTreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class LockManager<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(LockManager.class);

    private UpdatableTreeSet<Lock<T>> locks;
    private UpdatableTreeSet.Update<Lock<T>> defaultUpd = t -> t.setFrame(t.getNextFrame());

    private int currentFrame;
    private Lock<T> lastFrameLock;

    public LockManager(LockFactory<T> factory, int count) {
        this.locks = new UpdatableTreeSet<>(Lock.frameComparatorDesc);
        this.currentFrame = 0;
        for (int i = 0; i < count; i++) {
            locks.add(factory.create(i));
        }
    }

    public void release(Lock<T> lock) {
        if (lock == null){
            return;
        }
        if (lock.getState() == Lock.State.READ) {
            int inUseCount = lock.releaseRead();
            if (inUseCount == 0) {
                lock.setState(Lock.State.FREE);
            }
        } else if (lock.getState() == Lock.State.WRITE_READ){
            int nextFrame = lock.getNextFrame();

            if (nextFrame <= currentFrame){
                lock.resolve(lastFrameLock);
                lock.setNextFrame(++currentFrame);
                locks.update(lock, defaultUpd);
            } else {
                locks.update(lock, defaultUpd);
            }

            currentFrame = lock.getNextFrame();
            lastFrameLock = lock;
            lock.setState(Lock.State.FREE);
        }
    }

    public Lock<T> forRead() {
        for (Lock<T> lock : locks) {
            Lock.State state = lock.getState();
            if (state == Lock.State.FREE || state == Lock.State.READ) {
                lock.getRead();
                //get fresh input data??
                lock.setState(Lock.State.READ);
                return lock;
            }
        }
        return null;
    }

    public Lock<T> forWrite() {
        for (Iterator<Lock<T>> iterator = locks.descendingIterator(); iterator.hasNext(); ) {
            Lock<T> lock = iterator.next();
            Lock.State state = lock.getState();
            if (state == Lock.State.FREE) {
                lock.setNextFrame(currentFrame + 1);
                lock.setState(Lock.State.WRITE_READ);
                return lock;
            }
        }
        return null;
    }

    private void logLocks(){
        LOGGER.info("Current frame {}", currentFrame);
        LOGGER.info("Current last lock {}", lastFrameLock);
        LOGGER.info("All locks {}", locks);
    }

    public LockUser<T> createReadOnly() {
        return new LockUser<T>(this, true);
    }

    public LockUser<T> createReadWrite() {
        return new LockUser<T>(this, false);
    }
}

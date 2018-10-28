package com.theopus.xengine.nscheduler.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class Lock<T> {

    public static final Comparator<Lock<?>> frameComparatorDesc = (o1, o2) -> {
        //TODO: replace via Long.compareUnsigned()
        int i = o2.frame - o1.frame;
        if (i == 0) {
            return o1.id - o2.id;
        } else {
            return i;
        }
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(Lock.class);
    private final int id;
    private int inUse;
    private int frame;
    private int nextFrame;
    private Type type;
    private T of;

    public Lock(int id, T of) {
        this.id = id;
        this.of = of;
        this.type = Type.FREE;
    }

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getRead() {
        return ++inUse;
    }

    public int releaseRead() {
        return --inUse;
    }

    public void resolve(Lock<T> lastLock) {
        LOGGER.warn("{} copy from {} and reapply. This should be override.", this, lastLock);
    }

    public int getFrame() {
        return frame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getNextFrame() {
        return nextFrame;
    }

    public void setNextFrame(int nextFrame) {
        this.nextFrame = nextFrame;
    }

    public T getOf() {
        return of;
    }

    @Override
    public String toString() {
        return "Lock{" +
                "id=" + id +
                ", frame=" + frame +
                ", nextFrame=" + nextFrame +
                ", type=" + type +
                ", inUse=" + inUse +
                '}';
    }

    public enum Type {
        READ, WRITE_READ, FREE
    }
}

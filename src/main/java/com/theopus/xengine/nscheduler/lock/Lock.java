package com.theopus.xengine.nscheduler.lock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class Lock<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Lock.class);

    private final int id;
    private int inUse;

    private int frame;
    private int nextFrame;
    private State state;

    private T of;

    public Lock(int id, T of) {
        this.id = id;
        this.of = of;
        this.state = State.FREE;
    }

    public State getState() {
        return this.state;
    }

    public void setState(State state) {
        this.state = state;
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

    public enum State {
        READ, WRITE_READ, FREE
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

    public static final Comparator<Lock<?>> frameComparatorDesc = (o1, o2) -> {
        int i = o2.frame - o1.frame;
        if (i == 0) {
            return o1.id - o2.id;
        } else {
            return i;
        }
    };
    @Override
    public String toString() {
        return "Lock{" +
                "id=" + id +
                ", frame=" + frame +
                ", nextFrame=" + nextFrame +
                ", state=" + state +
                ", inUse=" + inUse +
                '}';
    }
}

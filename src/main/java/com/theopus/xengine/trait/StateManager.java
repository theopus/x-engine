package com.theopus.xengine.trait;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PriorityQueue;
import java.util.stream.IntStream;

public class StateManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateManager.class);

    private final EntityManagerFactory factory;
    private int lastFrame = 1;
    private int frameToBePrepared = 1;
    private PriorityQueue<State> states = new PriorityQueue<>(new State.FrameComparator());


    public StateManager(EntityManagerFactory factory, int statesCount) {
        this.factory = factory;
        IntStream.range(0, statesCount)
                .forEach(i -> this.states.add(new State(i, factory.create())));
    }


    public void release(State state) {
        LOGGER.debug("Released state {}", state);

        if (state.getLock() == LockType.READ_WRITE) {
            int targetFrame = state.getTargetFrame();
            if (targetFrame <= lastFrame) {
                LOGGER.warn("Found conflict, expected to be {}->{}, but got {}.", state.getFrame(), state.getTargetFrame(), lastFrame);
                State lastState = forRead();
                lastState.getEm().copyTo(state.getEm());
                state.getEm().reApplyTransformations();
                release(lastState);

                lastFrame += 1;
                state.setFrame(lastFrame);
                frameToBePrepared++;

                LOGGER.info("Eventually set frame {}", lastFrame);
            } else {
                lastFrame = targetFrame;
                state.setFrame(lastFrame);
                LOGGER.debug("Released {} frame", lastFrame);
            }

            LOGGER.debug("Last frame {}", lastFrame);
            
        }
        state.setLock(LockType.FREE);
        state.clearFrame();
        states.remove(state);
        states.add(state);
        LOGGER.debug("After release state {}", state);

    }

    public State forRead() {
        LOGGER.debug("Trying get state for read. queue: {}", states);

        for (State state : states)
            if (state.getLock() == LockType.FREE || state.getLock() == LockType.READ_ONLY) {
                state.setLock(LockType.READ_ONLY);
                LOGGER.debug("States: {}", states);
                LOGGER.debug("Got state for read {}, applied read lock", state);
                state.setTargetFrame(state.getTargetFrame());
                return state;
            }
        LOGGER.debug("Return 'null'. Not found available states for read {}", states);

        return null;
    }

    public State forWrite() {
        LOGGER.debug("Trying get state for write. queue: {}", states);

        for (State state : states) {
            if (state.getLock() == LockType.FREE) {
                if (state.getFrame() == lastFrame){
                    continue;
                }
                state.setLock(LockType.READ_WRITE);
                LOGGER.debug("Got state for write {}, applied write lock", state);
                state.setTargetFrame(++frameToBePrepared);
                LOGGER.debug("Target frame {}", state.getTargetFrame());
                return state;
            }
        }
        LOGGER.debug("Return 'null' .Not found available states for write {}", states);
        return null;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public PriorityQueue<State> getStates() {
        return states;
    }

    public enum LockType {
        READ_ONLY,
        READ_WRITE,
        FREE,
    }
}

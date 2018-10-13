package com.theopus.xengine.trait;

public class StateManager {

    private int lastFrame;
    private State state;

    public StateManager(EntityManager em) {
        this.state = new State(1, em);
    }

    public State acquireLastState(LockType type) {
        return null;
    }

    public int getLastFrame() {
        return lastFrame;
    }

    public void setLastFrame(int lastFrame) {
        this.lastFrame = lastFrame;
    }

    public void updateState(State state) {

    }

    public State getState() {
        return state;
    }

    enum LockType {
        READ_ONLY,
        READ_WRITE,
    }
}

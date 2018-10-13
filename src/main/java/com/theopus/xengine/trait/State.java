package com.theopus.xengine.trait;

import com.theopus.xengine.system.System;

public class State implements Comparable<State> {

    private final int id;
    private int frame;
    private int targetFrame;
    private StateManager.LockType lockType = StateManager.LockType.FREE;

    private EntityManager em;

    public State(int id) {
        this.id = id;
    }

    public State(int id, EntityManager em) {
        this.id = id;
        this.em = em;
    }

    public <T extends Trait> TraitEditor<T> getEditor(Class<T> renderTraitClass) {
        TraitMapper<T> mapper = em.getManager().getMapper(renderTraitClass);
        return mapper.getEditor();
    }


    public void attachTo(System system) {
        system.configurer().setRead(this);
    }

    public <T extends Trait> TraitMapper<T> getMapper(Class<T> renderTraitClass) {
        return em.getManager().getMapper(renderTraitClass);
    }

    public EntityManager getEm() {
        return em;
    }

    @Override
    public int compareTo(State o) {
        return o.frame - this.frame;
    }

    public StateManager.LockType getLock() {
        return lockType;
    }

    public void setLock(StateManager.LockType lock) {
        this.lockType = lock;
    }

    public void setTargetFrame(int targetFrame) {
        this.targetFrame = targetFrame;
    }

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", frame=" + frame +
                ", targetFrame=" + targetFrame +
                ", lockType=" + lockType +
                ", em=" + em +
                '}';
    }

    public int getTargetFrame() {
        return targetFrame;
    }

    public void setFrame(int frame) {
        this.frame = frame;
    }

    public int getFrame() {
        return frame;
    }
}

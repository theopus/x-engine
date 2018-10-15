package com.theopus.xengine.trait;

import com.theopus.xengine.system.System;

import java.util.Comparator;

public class State{

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

    public int getId() {
        return id;
    }

    public StateManager.LockType getLockType() {
        return lockType;
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

    public void clearFrame() {
        em.clearEditors();
    }

    public static class FrameComparator implements Comparator<State>{

        @Override
        public int compare(State o1, State o2) {
            return Integer.compareUnsigned(o2.getFrame(), o1.getFrame());
        }
    }

    public static class TargetFrameComparator implements Comparator<State>{

        @Override
        public int compare(State o1, State o2) {
            return Integer.compareUnsigned(o1.getTargetFrame(), o2.getTargetFrame());
        }
    }
}

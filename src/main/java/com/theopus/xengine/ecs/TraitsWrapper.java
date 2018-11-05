package com.theopus.xengine.ecs;

import com.theopus.xengine.trait.Trait;

public class TraitsWrapper<T extends Trait> {

    private int gen;
    private int nextGen;
    private Class<T> traitClass;
    private WrapperStatus status;
    private int readCount;

    public TraitsWrapper(Class<T> traitClass) {
        this.traitClass = traitClass;
    }

    public T get(int entity) {
        return null;
    }

    public Class<T> getTraitClass() {
        return traitClass;
    }

    int releaseRead() {
        return --readCount;
    }

    int getRead() {
        return ++readCount;
    }

    public void setStatus(WrapperStatus status) {
        this.status = status;
    }

    public WrapperStatus getStatus() {
        return status;
    }

    public void setNextGen(int gen) {
        nextGen = gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public int getGen() {
        return gen;
    }

    public int getNextGen() {
        return nextGen;
    }
}

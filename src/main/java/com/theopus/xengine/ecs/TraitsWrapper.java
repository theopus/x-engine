package com.theopus.xengine.ecs;

import com.theopus.xengine.trait.Trait;

import java.util.Comparator;

public class TraitsWrapper<T extends Trait> {

    public static final Comparator<TraitsWrapper<?>> genComparatorDesc = (o1, o2) -> {
        //TODO: replace via Long.compareUnsigned()
        int i = o2.gen - o1.gen;
        if (i == 0) {
            return o1.id - o2.id;
        } else {
            return i;
        }
    };

    private int id;
    private int gen;
    private int nextGen;
    private Class<T> traitClass;
    private WrapperStatus status;
    private int readCount;

    public TraitsWrapper(Class<T> traitClass, int id) {
        this.traitClass = traitClass;
        this.id = id;
        this.status = WrapperStatus.FREE;
    }

    public void resolve(TraitsWrapper<T> lastGenWrapper) {

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

    @Override
    public String toString() {
        return "TraitsWrapper{" +
                "id=" + id +
                ", gen=" + gen +
                ", nextGen=" + nextGen +
                ", traitClass=" + traitClass +
                ", status=" + status +
                ", readCount=" + readCount +
                '}';
    }
}

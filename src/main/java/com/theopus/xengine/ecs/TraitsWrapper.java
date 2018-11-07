package com.theopus.xengine.ecs;

import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.utils.Reflection;

import java.util.*;

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

    private BitSet available;
    private Map<Integer, T> traits;
    private List<Transformation<T>> transformations;

    public TraitsWrapper(Class<T> traitClass, int id) {
        this.traitClass = traitClass;
        this.id = id;
        this.status = WrapperStatus.FREE;
        this.available = new BitSet();
        this.traits = new HashMap<>();
        this.transformations = new ArrayList<>();
    }

    public void resolve(TraitsWrapper<T> lastGenWrapper) {

    }

    public boolean has(int entityId) {
        return available.get(entityId);
    }

    public T get(int entityId) {
        boolean contains = available.get(entityId);
        if (contains) {
            return traits.get(entityId);
        } else {
            T trait = Reflection.newInstance(traitClass);
            available.set(entityId);
            traits.put(entityId, trait);
            return trait;
        }
    }

    public T remove(int entityId) {
        available.set(entityId, false);
        return traits.remove(entityId);
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

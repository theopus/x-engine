package com.theopus.xengine.trait;

import java.util.BitSet;

public class EntityManager {

    private TraitManager manager;
    private BitSet bitSet = new BitSet();

    private int counter = 0;

    public EntityManager(TraitManager manager) {
        this.manager = manager;
    }

    public synchronized int createEntity(){
        int id = counter;
        bitSet.set(id, true);
        counter++;
        return id;
    }

    public <T extends Trait> T createTrait(int entity, Class<T> tClass) {
        bitSet.set(entity, true);
        TraitMapper<T> mapper = manager.getMapper(tClass);
        return mapper.get(entity);
    }

    public TraitManager getManager() {
        return manager;
    }
}

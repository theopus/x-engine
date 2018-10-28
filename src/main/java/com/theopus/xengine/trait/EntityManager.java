package com.theopus.xengine.trait;

import java.util.BitSet;
import java.util.Map;

public class EntityManager {

    private TraitManager manager;
    private BitSet bitSet = new BitSet();

    private int counter = 0;

    public EntityManager(TraitManager manager) {
        this.manager = manager;
    }

    public synchronized int createEntity() {
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

    public TraitManager getTraitManger() {
        return manager;
    }

    public void clearEditors() {
        manager.clearEditors();
    }

    public <T extends Trait> EntityManager copyTo(EntityManager em) {
        TraitManager targetManager = em.getTraitManger();
        for (TraitMapper<Trait> traitMapper : this.getTraitManger().traitMappers()) {
            TraitEditor editor = targetManager.getMapper(traitMapper.getTraitClass()).getEditor();
            Map<Integer, Trait> traits = traitMapper.traits();
            traits.entrySet().forEach(it -> editor.copy(it.getKey(), it.getValue()));
        }
        return this;
    }

    public BitSet entitiesWith(Class<? extends Trait>... traits) {
        if (traits.length == 0) {
            return new BitSet();
        }
        BitSet newSet = new BitSet();
        newSet.or(manager.getMapper(traits[0]).traitsBits());

        for (int i = 1; i < traits.length; i++) {
            newSet.and(manager.getMapper(traits[i]).traitsBits());
        }
        return newSet;
    }

    public void reApplyTransformations() {
        manager.reApplyTransformations();
    }

    public <T extends Trait> TraitMapper<T> getMapper(Class<T> traitClass) {
        return manager.getMapper(traitClass);
    }

    public <T extends Trait> TraitEditor<T> getEditor(Class<T> traitClass) {
        return getMapper(traitClass).getEditor();
    }
}

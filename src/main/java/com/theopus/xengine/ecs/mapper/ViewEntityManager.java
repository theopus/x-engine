package com.theopus.xengine.ecs.mapper;

import com.theopus.xengine.ecs.EntitySystemManager;
import com.theopus.xengine.ecs.Trait;
import com.theopus.xengine.ecs.Transformation;
import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewEntityManager implements TaskComponent, Bits {
    private final Map<? extends Class<? extends Trait>, ? extends TraitMapper<? extends Trait>> mappers;
    private final BitSet entities = new BitSet();

    public ViewEntityManager(EntitySystemManager entitySystemManager, Set<Class<? extends Trait>> classes) {
        this.mappers = classes.stream()
                .map(entitySystemManager::getReadMapper)
                .collect(Collectors.toMap(TraitMapper::getTraitClass, i -> i));
    }

    @Override
    public boolean prepare() {
        boolean status = true;
        entities.clear();
        for (TraitMapper<? extends Trait> value : mappers.values()) {
            status = value.prepare();
            entities.or(value.bits());
            if (!status) {
                rollback();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean rollback() {
        return finish();
    }

    @Override
    public boolean finish() {
        for (TraitMapper<? extends Trait> value : mappers.values()) {
            value.finish();
        }
        return true;
    }

    public <T extends Trait> T get(int entity, Class<T> trait){
        return (T) mappers.get(trait).get(entity);
    }

    public BitSet bits() {
        return entities;
    }
}

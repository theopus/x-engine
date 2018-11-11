package com.theopus.xengine.ecs.mapper;

import com.theopus.xengine.ecs.EntitySystemManager;
import com.theopus.xengine.ecs.Trait;
import com.theopus.xengine.ecs.Transformation;
import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityManager implements TaskComponent, Bits {

    private final Map<? extends Class<? extends Trait>, ? extends WriteTraitMapper<? extends Trait>> mappers;
    private final BitSet entities = new BitSet();

    public EntityManager(EntitySystemManager entitySystemManager, Set<Class<? extends Trait>> classes) {
        this.mappers = classes.stream()
                .map(entitySystemManager::getWriteMapper)
                .collect(Collectors.toMap(WriteTraitMapper::getTraitClass, i -> i));
    }

    @Override
    public boolean prepare() {
        boolean status = true;
        entities.clear();
        for (WriteTraitMapper<? extends Trait> value : mappers.values()) {
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
        for (WriteTraitMapper<? extends Trait> value : mappers.values()) {
            value.finish();
        }
        return true;
    }

    public int create() {
        int size = entities.length();
        entities.set(size);
        return size;
    }

    public <T extends Trait> T create(Class<T> trait) {
        WriteTraitMapper<? extends Trait> writeTraitMapper = mappers.get(trait);
        return (T) writeTraitMapper.get(entities.length());
    }

    public <T extends Trait> T create(int entity, Class<T> trait) {
        WriteTraitMapper<? extends Trait> writeTraitMapper = mappers.get(trait);
        return (T) writeTraitMapper.get(entity);
    }

    public <T extends Trait> void transform(int entity, Class<T> trait, Transformation<T> transformation){
        WriteTraitMapper<T> writeTraitMapper = (WriteTraitMapper<T>) mappers.get(trait);
        writeTraitMapper.transform(entity, transformation);
    }

    public BitSet bits() {
        return entities;
    }
}

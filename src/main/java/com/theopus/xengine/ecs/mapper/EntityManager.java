package com.theopus.xengine.ecs.mapper;

import com.theopus.xengine.ecs.EntitySystemManager;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.ecs.mapper.WriteTraitMapper;
import com.theopus.xengine.nscheduler.task.TaskComponent;
import com.theopus.xengine.trait.Trait;

import java.util.BitSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityManager implements TaskComponent {

    private final Map<? extends Class<? extends Trait>, ? extends WriteTraitMapper<? extends Trait>> mappers;
    private BitSet entities = new BitSet();

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
            if (!status){
                rollback();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean rollback() {
        finish();
        return true;
    }

    @Override
    public boolean finish() {
        for (WriteTraitMapper<? extends Trait> value : mappers.values()) {
            value.finish();
        }
        return true;
    }

    public BitSet entities() {
        return entities;
    }
}

package com.theopus.xengine.ecs;

import com.theopus.xengine.nscheduler.task.TaskComponent;
import com.theopus.xengine.trait.Trait;

import java.util.Set;

public class TraitMultiMapper implements TaskComponent {
    private final EntitySystemManager entitySystemManager;
    private final Set<Class<? extends Trait>> keySet;

    public TraitMultiMapper(EntitySystemManager entitySystemManager, Set<Class<? extends Trait>> keySet) {

        this.entitySystemManager = entitySystemManager;
        this.keySet = keySet;
    }

    @Override
    public boolean prepare() {
        return false;
    }

    @Override
    public boolean rollback() {
        return false;
    }

    @Override
    public boolean finish() {
        return false;
    }
}

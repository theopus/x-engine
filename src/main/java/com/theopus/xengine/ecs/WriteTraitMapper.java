package com.theopus.xengine.ecs;

import com.theopus.xengine.nscheduler.task.TaskComponent;

public class WriteTraitMapper<T> implements TaskComponent {
    private final EntitySystemManager entitySystemManager;
    private final Class<T> traitClass;

    public WriteTraitMapper(EntitySystemManager entitySystemManager, Class<T> traitClass) {

        this.entitySystemManager = entitySystemManager;
        this.traitClass = traitClass;
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

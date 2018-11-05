package com.theopus.xengine.ecs;

import com.theopus.xengine.nscheduler.task.TaskComponent;
import com.theopus.xengine.trait.Trait;

public class TraitMapper<T extends Trait> implements TaskComponent {

    private final EntitySystemManager manager;
    private final Class<T> traitClass;
    private TraitsWrapper<T> wrapper;

    public TraitMapper(EntitySystemManager manager, Class<T> traitClass) {
        this.manager = manager;
        this.traitClass = traitClass;
    }

    @Override
    public boolean prepare() {
        wrapper = manager.wrapperForRead(traitClass);
        return wrapper != null;
    }

    @Override
    public boolean rollback() {
        manager.releaseRead(wrapper);
        return true;
    }

    @Override
    public boolean finish() {
        manager.releaseRead(wrapper);
        return true;
    }

    public T get(int entity) {
        return wrapper.get(entity);
    }
}

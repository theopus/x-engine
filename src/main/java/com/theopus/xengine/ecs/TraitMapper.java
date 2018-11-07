package com.theopus.xengine.ecs;

import com.theopus.xengine.nscheduler.task.TaskComponent;
import com.theopus.xengine.trait.Trait;

public class TraitMapper<T extends Trait> implements TaskComponent {

    private final EntitySystemManager.WrappersPack<T> pack;
    private final Class<T> traitClass;
    private TraitsWrapper<T> wrapper;

    public TraitMapper(EntitySystemManager pack, Class<T> traitClass) {
        this.pack = pack.pack(traitClass);
        this.traitClass = traitClass;
    }

    @Override
    public boolean prepare() {
        wrapper = pack.wrapperForRead();
        return wrapper != null;
    }

    @Override
    public boolean rollback() {
        pack.releaseRead(wrapper);
        wrapper = null;
        return true;
    }

    @Override
    public boolean finish() {
        pack.releaseRead(wrapper);
        wrapper = null;
        return true;
    }

    public T get(int entity) {
        return wrapper.get(entity);
    }
}

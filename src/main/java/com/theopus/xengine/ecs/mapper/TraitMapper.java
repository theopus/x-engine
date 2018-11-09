package com.theopus.xengine.ecs.mapper;

import com.theopus.xengine.ecs.EntitySystemManager;
import com.theopus.xengine.ecs.TraitsWrapper;
import com.theopus.xengine.nscheduler.task.TaskComponent;
import com.theopus.xengine.trait.Trait;

import java.util.BitSet;
import java.util.Iterator;

public class TraitMapper<T extends Trait> implements TaskComponent, Iterable<T> {

    private final EntitySystemManager.WrappersPack<T> pack;
    private final Class<T> traitClass;
    private TraitsWrapper<T> wrapper;

    public TraitMapper(EntitySystemManager pack, Class<T> traitClass) {
        this.pack = pack.pack(traitClass);
        this.traitClass = traitClass;
    }

    @Override
    public Iterator<T> iterator(){
        BitSet bits = bits();
        return new Iterator<T>() {
            int i = -1;
            @Override
            public boolean hasNext() {
                i = bits.nextSetBit(i+1);
                return i != -1;
            }

            @Override
            public T next() {
                return get(i);
            }
        };
    }

    public BitSet bits(){
        return wrapper.bits();
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
        wrapper.flush();
        wrapper = null;
        return true;
    }

    public T get(int entity) {
        return wrapper.get(entity);
    }

    public Class<T> getTraitClass() {
        return traitClass;
    }
}

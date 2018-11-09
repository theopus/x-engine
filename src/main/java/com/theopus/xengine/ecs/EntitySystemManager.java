package com.theopus.xengine.ecs;

import com.theopus.xengine.ecs.mapper.EntityManager;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.ecs.mapper.WriteTraitMapper;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.utils.UpdatableTreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntitySystemManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySystemManager.class);

    Map<Class<? extends Trait>, WrappersPack<? extends Trait>> packs;

    public EntitySystemManager(List<Class<? extends Trait>> traits, int number) {
        packs = new HashMap<>();
        traits.forEach(c -> {
            TraitsWrapper[] wrappers = IntStream
                    .range(0, number)
                    .mapToObj(i -> new TraitsWrapper<>(c, i))
                    .collect(Collectors.toList())
                    .toArray(new TraitsWrapper[number]);
            packs.put(c, new WrappersPack<>(wrappers));
        });
    }

    public <T extends Trait> TraitMapper<T> getReadMapper(Class<T> traitClass) {
        return new TraitMapper<>(this, traitClass);
    }

    public <T extends Trait> WriteTraitMapper<T> getWriteMapper(Class<T> traitClass) {
        return new WriteTraitMapper<>(this, traitClass);
    }

    public <T extends Trait> WrappersPack<T> pack(Class<T> tclass) {
        return (WrappersPack<T>) packs.get(tclass);
    }

    public EntityManager getEntityManager() {
        return new EntityManager(this, packs.keySet());
    }

    public class WrappersPack<WT extends Trait> {
        private UpdatableTreeSet.Update<TraitsWrapper<WT>> defaultUpd = t -> t.setGen(t.getNextGen());

        private UpdatableTreeSet<TraitsWrapper<WT>> wrappers;
        private int currentGen;
        private TraitsWrapper<WT> lastGenWrapper;

        public WrappersPack(TraitsWrapper<WT>... wrappers) {
            this.wrappers = new UpdatableTreeSet<>(TraitsWrapper.genComparatorDesc);
            this.wrappers.addAll(Arrays.asList(wrappers));
        }

        public TraitsWrapper<WT> wrapperForRead() {
            for (TraitsWrapper<WT> wrapper : wrappers) {
                WrapperStatus status = wrapper.getStatus();
                if (status == WrapperStatus.FREE || status == WrapperStatus.READ) {
                    wrapper.getRead();
                    return wrapper;
                }
            }
            return null;
        }

        public TraitsWrapper<WT> wrapperForWrite() {
            for (Iterator<TraitsWrapper<WT>> iterator = wrappers.descendingIterator(); iterator.hasNext(); ) {
                TraitsWrapper<WT> wrapper = iterator.next();
                WrapperStatus status = wrapper.getStatus();
                if (status == WrapperStatus.FREE) {
                    wrapper.getWrite();
                    wrapper.setNextGen(currentGen + 1);
                    return wrapper;
                }
            }
            return null;
        }

        public int releaseRead(TraitsWrapper<WT> wrapper) {
            if (wrapper == null){
                return 0;
            }
            int inUseCount = wrapper.releaseRead();
            if (inUseCount == 0) {
                wrapper.setStatus(WrapperStatus.FREE);
            }
            return inUseCount;
        }

        public void releaseWrite(TraitsWrapper<WT> wrapper) {
            if (wrapper == null){
                return;
            }
            int nextFrame = wrapper.getNextGen();
            if (nextFrame <= currentGen) {
                wrapper.resolve(lastGenWrapper);
                wrapper.setNextGen(++currentGen);
                wrappers.update(wrapper, defaultUpd);
            } else {
                wrappers.update(wrapper, defaultUpd);
            }
            currentGen = wrapper.getGen();
            lastGenWrapper = wrapper;
            wrapper.setStatus(WrapperStatus.FREE);
        }
    }
}

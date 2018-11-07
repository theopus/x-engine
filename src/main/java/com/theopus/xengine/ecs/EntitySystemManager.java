package com.theopus.xengine.ecs;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.utils.UpdatableTreeSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class EntitySystemManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntitySystemManager.class);

    private Map<Class<? extends Trait>, WrappersPack<? extends Trait>> packs;

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

    public TraitMultiMapper getReadManager() {
        return new TraitMultiMapper(this, packs.keySet());
    }

    public WriteTraitMultiMapper getWriteManager() {
        return new WriteTraitMultiMapper(this, packs.keySet());
    }

    public TraitMultiMapper getReadManager(List<Class<? extends Trait>> classes) {
        if (!packs.keySet().containsAll(classes)) {
            throw new RuntimeException("Passed classes not configured. Intersection " + Sets.intersection(new HashSet<>(classes), packs.keySet()));
        }
        return new TraitMultiMapper(this, Sets.intersection(new HashSet<>(classes), packs.keySet()));
    }

    public WriteTraitMultiMapper getWriteManager(List<Class<? extends Trait>> classes) {
        if (!packs.keySet().containsAll(classes)) {
            throw new RuntimeException("Passed classes not configured. Intersection " + Sets.intersection(new HashSet<>(classes), packs.keySet()));
        }
        return new WriteTraitMultiMapper(this, Sets.intersection(new HashSet<>(classes), packs.keySet()));
    }

    <T extends Trait> WrappersPack<T> pack(Class<T> tclass) {
        return (WrappersPack<T>) packs.get(tclass);
    }

    class WrappersPack<WT extends Trait> {
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
                    wrapper.setStatus(WrapperStatus.READ);
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
                    wrapper.setNextGen(currentGen + 1);
                    wrapper.setStatus(WrapperStatus.FREE);
                    return wrapper;
                }
            }
            return null;
        }

        public int releaseRead(TraitsWrapper<WT> wrapper) {
            Preconditions.checkNotNull(wrapper);
            int inUseCount = wrapper.releaseRead();
            if (inUseCount == 0) {
                wrapper.setStatus(WrapperStatus.FREE);
            }
            return inUseCount;
        }

        public void releaseWrite(TraitsWrapper<WT> wrapper) {
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

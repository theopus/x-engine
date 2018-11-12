package com.theopus.xengine.ecs;

import com.theopus.xengine.utils.BitSetUtils;
import com.theopus.xengine.utils.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class TraitsWrapper<T extends Trait> {

    public static final Comparator<TraitsWrapper<?>> genComparatorDesc = (o1, o2) -> {
        //TODO: replace via Long.compareUnsigned()
        int i = o2.gen - o1.gen;
        if (i == 0) {
            return o1.id - o2.id;
        } else {
            return i;
        }
    };
    private static final Logger LOGGER = LoggerFactory.getLogger(TraitsWrapper.class);
    private int id;
    private int gen;
    private int nextGen;
    private Class<T> traitClass;
    private WrapperStatus status;
    private int readCount;

    private BitSet available;
    private BitSet created;
    private Map<Integer, T> traits;
    private List<Transformation<T>> transformations;

    public TraitsWrapper(Class<T> traitClass, int id) {
        this.traitClass = traitClass;
        this.id = id;
        this.status = WrapperStatus.FREE;
        this.available = new BitSet();
        this.created = new BitSet();
        this.traits = new HashMap<>();
        this.transformations = new ArrayList<>();
    }

    public void resolve(TraitsWrapper<T> lastGenWrapper) {
        LOGGER.warn("Resolving expected to be [{}->{}], was [{}] on [{}]", gen, nextGen, lastGenWrapper.gen, traitClass);
        this.copyFrom(lastGenWrapper);
        transformations.forEach(t -> t.apply(this));
    }

    public boolean has(int entityId) {
        return available.get(entityId);
    }

    public T get(int entityId) {
        boolean contains = available.get(entityId);
        if (contains) {
            return traits.get(entityId);
        } else {
            T trait = Reflection.newInstance(traitClass);
            available.set(entityId);
            created.set(entityId);
            traits.put(entityId, trait);
            return trait;
        }
    }

    public T remove(int entityId) {
        available.set(entityId, false);
        return traits.remove(entityId);
    }

    public Class<T> getTraitClass() {
        return traitClass;
    }

    int releaseRead() {
        return --readCount;
    }

    int getRead() {
        status = WrapperStatus.READ;
        return ++readCount;
    }

    public WrapperStatus getStatus() {
        return status;
    }

    public void setStatus(WrapperStatus status) {
        this.status = status;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public int getNextGen() {
        return nextGen;
    }

    public void setNextGen(int gen) {
        nextGen = gen;
    }

    public void addTransformation(Transformation<T> transformation) {
        transformations.add(transformation);
    }

    public BitSet bits() {
        return available;
    }

    public void copyFrom(TraitsWrapper<T> from) {
        BitSet fromBits = from.bits();
        BitSet toBits = this.bits();
        //TODO [PERFORMANCE]:Can be pooled
        BitSet xor = BitSetUtils.xor(fromBits, toBits);

        // 10101 - from
        // 10011 - to

        // 00110 - xor0

        // 00010 - xor1 - toDelete

        if (!xor.isEmpty()) {
            xor.xor(fromBits);
            xor.stream().forEach(this::remove);
        }
//        System.out.println(from);
//        System.out.println(this);
        from.traits.forEach((k, v) -> v.duplicateTo(get(k)));
        created.clear();
    }

    public void addAndApply(int entity, Transformation<T> transformation) {
        addTransformation(transformation);
        transformation.apply(this);
    }

    public void getWrite() {
        status = WrapperStatus.WRITE;
    }

    public void flush() {
        transformations.clear();
        created.clear();
    }

    public BitSet getCreated() {
        return created;
    }

    public T create(Class<T> trait) {
        return get(available.length());
    }
}

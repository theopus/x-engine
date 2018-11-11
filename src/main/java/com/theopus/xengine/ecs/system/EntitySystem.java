package com.theopus.xengine.ecs.system;

import com.theopus.xengine.ecs.mapper.Bits;
import com.theopus.xengine.nscheduler.Context;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public abstract class EntitySystem extends TaskSystem {

    private List<Bits> bits;
    private BitSet holder;

    public EntitySystem(Context context, boolean repetable, float rate) {
        super(context, repetable, rate);
        holder = new BitSet();
        bits = new ArrayList<>();
    }

    @Override
    public void init() {
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Bits.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    bits.add((Bits) field.get(this));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void process() {
        holder.clear();
        holder.or(bits.get(0).bits());
        for (Bits bit : bits) {
            holder.and(bit.bits());
        }
        process(holder);
    }

    public abstract void process(BitSet entities);
}

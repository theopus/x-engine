package com.theopus.xengine.entity;

import com.theopus.xengine.trait.PositionTrait;
import com.theopus.xengine.trait.RenderTrait;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.utils.Reflection;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class EntityManager {

    private Map<Integer, RenderTrait> renderTraitMap = new HashMap<>();
    private Map<Integer, PositionTrait> positionTraitMap = new HashMap<>();
    private BitSet bitSet = new BitSet();

    private int counter = 0;


    public synchronized int createEntity(){
        int id = counter;
        bitSet.set(id, true);
        counter++;
        return id;
    }

    @SuppressWarnings("unchecked")
    public <T extends Trait> T createTrait(int entity, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        if (!bitSet.get(entity)){
            throw new RuntimeException("Entity is not constructed");
        }
        T t;
        if (tClass == RenderTrait.class){
            t = Reflection.newInstance(tClass);
            renderTraitMap.put(entity, (RenderTrait) t);
        } else if (tClass == PositionTrait.class){
            t = Reflection.newInstance(tClass);
            positionTraitMap.put(entity, (PositionTrait) t);
        } else {
            throw new RuntimeException("Not supported = " + tClass);
        }
        return t;
    }

}

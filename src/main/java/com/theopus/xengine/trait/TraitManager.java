package com.theopus.xengine.trait;

import com.theopus.xengine.utils.Reflection;

import java.util.HashMap;
import java.util.Map;

public class TraitManager {

    private Map<Class<? extends Trait>, TraitMapper> mappers;

    @SuppressWarnings("unchecked")
    public TraitManager(Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> traitsMap) {
        mappers = new HashMap<>();
        for (Class tratisClass : traitsMap.keySet()) {
            mappers.put(tratisClass, new TraitMapper(tratisClass, Reflection.newInstance(traitsMap.get(tratisClass))));
        }
    }


    @SuppressWarnings("unchecked")
    public <T extends Trait> TraitMapper<T> getMapper(Class<T> traitClass) {
        return mappers.get(traitClass);
    }
}

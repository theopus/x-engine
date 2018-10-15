package com.theopus.xengine.trait;

import com.theopus.xengine.utils.Reflection;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

    public Collection<TraitMapper> traitMappers(){
        return mappers.values();
    }

    public Stream<TraitEditor> traitEditors(){
        return traitMappers().stream().map(TraitMapper::getEditor);
    }


    public void clearEditors(){
        mappers.values().forEach(TraitMapper::clearEditor);
    }

    public void reApplyTransformations() {
        for (TraitMapper traitMapper : mappers.values()) {
            TraitEditor<? extends Trait> editor = traitMapper.getEditor();
            editor.transformations.forEach(transformation -> transformation.transform(traitMapper));
        }

    }
}

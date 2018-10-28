package com.theopus.xengine.trait;

import java.util.ArrayList;
import java.util.List;

public class TraitEditor<Trait extends com.theopus.xengine.trait.Trait> {
    protected TraitMapper<Trait> mapper;
    protected List<Transformation<Trait>> transformations = new ArrayList<>();

    public TraitEditor<Trait> with(TraitMapper<Trait> mapper) {
        this.mapper = mapper;
        return this;
    }

    public List<Transformation<Trait>> getTransformations() {
        return transformations;
    }

    public void copy(int entityId, Trait from) {
        Transformation<Trait> action = (mapper) -> {
            Trait to = mapper.get(entityId);
            from.duplicateTo(to);
        };
//        transformations.add(action);
        action.transform(mapper);
    }

    public void clear() {
        transformations.clear();
    }
}

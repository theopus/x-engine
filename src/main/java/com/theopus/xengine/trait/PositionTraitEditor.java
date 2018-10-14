package com.theopus.xengine.trait;

import java.util.List;

public class PositionTraitEditor extends TraitEditor<PositionTrait> {

    public void rotateZ(int entityId, float rotZ) {
        Transformation<PositionTrait> action = (mapper) -> {
            PositionTrait ptrait = mapper.get(entityId);
            ptrait.setRotZ(ptrait.getRotZ() + rotZ);
        };
        transformations.add(action);
        action.transform(mapper);
    }

    public List<Transformation<PositionTrait>> transformations(){
        return transformations;
    }

    public void copy(int entityId, PositionTrait from) {
        Transformation<PositionTrait> action = (mapper) -> {
            PositionTrait to = mapper.get(entityId);
            from.duplicateTo(to);
        };
        transformations.add(action);
        action.transform(mapper);
    }
}

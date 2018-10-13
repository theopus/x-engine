package com.theopus.xengine.trait;

import java.util.ArrayList;
import java.util.List;

public class PositionTraitEditor extends TraitEditor<PositionTrait> {

    private List<Transformation<PositionTrait>> transformations = new ArrayList<>();


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
}

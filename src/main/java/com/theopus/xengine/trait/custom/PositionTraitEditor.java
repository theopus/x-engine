package com.theopus.xengine.trait.custom;

import com.theopus.xengine.trait.TraitEditor;
import com.theopus.xengine.trait.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PositionTraitEditor extends TraitEditor<PositionTrait> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionTraitEditor.class);

    public void rotateZ(int entityId, float rotZ) {
        Transformation<PositionTrait> action = (mapper) -> {
            PositionTrait ptrait = mapper.get(entityId);
            ptrait.setRotZ(ptrait.getRotZ() + rotZ);
            ptrait.changed();
        };
        transformations.add(action);
        action.transform(mapper);
    }

    public List<Transformation<PositionTrait>> transformations() {
        return transformations;
    }

    public void rotateSpeed(int entityId, float speed) {
        Transformation<PositionTrait> action = (mapper) -> {
            PositionTrait ptrait = mapper.get(entityId);
            ptrait.setRotSpeed(speed);
            ptrait.changed();
        };
        transformations.add(action);
        action.transform(mapper);

    }
}

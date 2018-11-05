package com.theopus.client.ecs.trait;

import com.theopus.xengine.trait.TraitEditor;
import com.theopus.xengine.trait.Transformation;
import com.theopus.xengine.utils.Maths;
import org.joml.Vector3f;

import java.util.List;

public class WorldPositionTraitEditor extends TraitEditor<WorldPositionTrait> {

    public WorldPositionTraitEditor() {
    }

    public WorldPositionTraitEditor(List<Transformation<WorldPositionTrait>> transformations) {
        this.transformations = transformations;
    }

    public void transformWith(int entityId, Vector3f position, float xRot, float yRot, float zRot, float scale) {
        Transformation<WorldPositionTrait> action = (mapper) -> {
            WorldPositionTrait worldPositionTrait = mapper.get(entityId);
            Maths.applyTransformations(
                    position,
                    xRot,
                    yRot,
                    zRot,
                    scale,
                    worldPositionTrait.getTransformation()
            );
            worldPositionTrait.changed();
        };
        transformations.add(action);
        action.transform(mapper);
    }
}

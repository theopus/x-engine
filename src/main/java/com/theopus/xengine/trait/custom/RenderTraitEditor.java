package com.theopus.xengine.trait.custom;

import com.theopus.xengine.trait.TraitEditor;
import com.theopus.xengine.trait.Transformation;
import com.theopus.xengine.utils.Maths;
import org.joml.Vector3f;

import java.util.List;

public class RenderTraitEditor extends TraitEditor<RenderTrait> {

    public RenderTraitEditor() {
    }

    public RenderTraitEditor(List<Transformation<RenderTrait>> transformations) {
        this.transformations = transformations;
    }

    public void transformWith(int entityId, Vector3f position, float xRot, float yRot, float zRot, float scale) {
        Transformation<RenderTrait> action = (mapper) -> {
            RenderTrait renderTrait = mapper.get(entityId);
            Maths.applyTransformations(
                    position,
                    xRot,
                    yRot,
                    zRot,
                    scale,
                    renderTrait.getTransformation()
            );
            renderTrait.changed();
        };
        transformations.add(action);
        action.transform(mapper);
    }
}

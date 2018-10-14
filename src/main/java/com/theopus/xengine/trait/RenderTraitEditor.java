package com.theopus.xengine.trait;

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
        };
        transformations.add(action);
        action.transform(mapper);
    }

    public void copy(int entityId, RenderTrait from) {
        Transformation<RenderTrait> action = (mapper) -> {
            RenderTrait to = mapper.get(entityId);
            from.duplicateTo(to);
        };
        transformations.add(action);
        action.transform(mapper);
    }
}

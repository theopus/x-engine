package com.theopus.xengine.trait;

import com.theopus.xengine.utils.Maths;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RenderTraitEditor extends TraitEditor<RenderTrait> {

    private List<Transformation<RenderTrait>> transformations = new ArrayList<>();

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

    private List<Transformation<RenderTrait>> transformations(){
        return transformations;
    }
}

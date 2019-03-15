package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.utils.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelMatrixSystem extends IntervalIteratingSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMatrixSystem.class);

    private ComponentMapper<TransformationMatrix> mModelMatrix;
    private ComponentMapper<Transformation> mPosition;

    public ModelMatrixSystem() {
        super(Aspect.all(Transformation.class, TransformationMatrix.class), 10);
    }

    @Override
    protected void process(int entityId) {
        TransformationMatrix transformationMatrix = mModelMatrix.get(entityId);
        Transformation transformation = mPosition.get(entityId);

        Maths.applyTransformations(
                transformation.position,
                transformation.rotation.x,
                transformation.rotation.y,
                transformation.rotation.z,
                transformation.scale,
                transformationMatrix.model);
    }
}

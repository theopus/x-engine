package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.utils.Maths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelMatrixSystem extends IntervalIteratingSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModelMatrixSystem.class);

    private ComponentMapper<ModelMatrix> mModelMatrix;
    private ComponentMapper<Position> mPosition;

    public ModelMatrixSystem() {
        super(Aspect.all(), 1000);
    }

    @Override
    protected void process(int entityId) {
        LOGGER.info("Mtx");
        ModelMatrix modelMatrix = mModelMatrix.get(entityId);
        Position position = mPosition.get(entityId);

        Maths.applyTransformations(
                position.position,
                position.rotation.x,
                position.rotation.y,
                position.rotation.z,
                position.scale,
                modelMatrix.model);
    }
}

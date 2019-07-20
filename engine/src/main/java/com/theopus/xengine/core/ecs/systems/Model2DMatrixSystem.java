package com.theopus.xengine.core.ecs.systems;

import org.joml.Vector2f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.Transformation2D;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.ecs.components.TransformationMatrix2D;
import com.theopus.xengine.core.events.Subscriber;
import com.theopus.xengine.core.platform.FramebufferEvent;
import com.theopus.xengine.core.utils.Maths;

public class Model2DMatrixSystem extends IntervalIteratingSystem implements Subscriber<FramebufferEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Model2DMatrixSystem.class);

    private ComponentMapper<TransformationMatrix2D> mModelMatrix;
    private ComponentMapper<Transformation2D> mTransformation;

    private Vector2f buffer = new Vector2f();
    private float aspectRatio = 1;

    public Model2DMatrixSystem() {
        super(Aspect.all(TransformationMatrix2D.class, Transformation2D.class), 10);
    }

    @Override
    protected void process(int entityId) {
        TransformationMatrix2D transformationMatrix = mModelMatrix.get(entityId);
        Transformation2D transformation = mTransformation.get(entityId);

        buffer.set(transformation.position);

        buffer.mul(2, 2);
        buffer.sub(1,1);
        buffer.y *= -1;

//        transformation.scale.x = transformation.scale.y / aspectRatio;
        Maths.applyTransformations(
                buffer,
                transformation.rotation.y,
                transformation.scale,
                transformationMatrix.model);
    }

    @Override
    public void onEvent(FramebufferEvent framebufferEvent) {
        aspectRatio  = framebufferEvent.width * 1f / framebufferEvent.height;
    }
}

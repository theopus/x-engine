package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.utils.OpsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveSystem extends IntervalIteratingSystem {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveSystem.class);
    private final OpsCounter counter = new OpsCounter("Move");
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Transformation> mPosition;

    public MoveSystem() {
        super(Aspect.all(Transformation.class, Velocity.class), 10);
    }

    @Override
    protected void processSystem() {
        super.processSystem();
        counter.operateAndLog();
    }

    @Override
    protected void process(int entityId) {
        Transformation transformation = mPosition.get(entityId);
        Velocity velocity = mVelocity.get(entityId);

        transformation.position.x += velocity.position.x;
        transformation.position.y += velocity.position.y;
        transformation.position.z += velocity.position.z;

        transformation.rotation.x += velocity.rotation.x;
        transformation.rotation.y += velocity.rotation.y;
        transformation.rotation.z += velocity.rotation.z;

    }

}

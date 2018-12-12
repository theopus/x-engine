package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.utils.OpsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveSystem extends IntervalIteratingSystem {
    private final OpsCounter counter = new OpsCounter("Move");
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveSystem.class);

    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Position> mPosition;

    public MoveSystem() {
        super(Aspect.all(Position.class, Velocity.class), 10);
    }

    @Override
    protected void processSystem() {
        super.processSystem();
        counter.operateAndLog();
    }

    @Override
    protected void process(int entityId) {
        Position position = mPosition.get(entityId);
        Velocity velocity = mVelocity.get(entityId);

        position.position.x += velocity.position.x;
        position.position.y += velocity.position.y;
        position.position.z += velocity.position.z;

        position.rotation.x += velocity.rotation.x;
        position.rotation.y += velocity.rotation.y;
        position.rotation.z += velocity.rotation.z;

    }

}

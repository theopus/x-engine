package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.systems.IteratingSystem;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;

public class MoveSystem extends IteratingSystem {
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Position> mPosition;

    public MoveSystem() {
        super(Aspect.all(Position.class, Velocity.class));
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

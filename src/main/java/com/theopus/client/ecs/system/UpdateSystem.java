package com.theopus.client.ecs.system;

import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.VelocityTrait;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.ecs.Ecs;
import com.theopus.xengine.ecs.mapper.WriteTraitMapper;
import com.theopus.xengine.ecs.system.EntitySystem;
import com.theopus.xengine.event.EventManager;
import com.theopus.xengine.event.InputData;
import com.theopus.xengine.event.TopicReader;
import com.theopus.xengine.inject.Event;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.utils.Maths;
import com.theopus.xengine.utils.OpsCounter;
import org.joml.Vector3f;

import java.util.BitSet;
import java.util.concurrent.ThreadLocalRandom;

public class UpdateSystem extends EntitySystem {


    private OpsCounter counter = new OpsCounter("Update");

    @Ecs
    private WriteTraitMapper<PositionTrait> position;
    @Ecs
    private WriteTraitMapper<WorldPositionTrait> worldPosition;

    @Ecs
    private WriteTraitMapper<VelocityTrait> velocity;


    @Inject
    public UpdateSystem() {
        super(Context.WORK, true, 60);
    }

    @Override
    public void process(BitSet entities) {
        entities.stream().forEach(e -> {
            PositionTrait positionTrait = position.get(e);
            VelocityTrait velocityTrait = velocity.get(e);

            position.transform(e, w -> {
                PositionTrait trait = w.get(e);
                Vector3f position = trait.getPosition();

                position.x += velocityTrait.getTranslation().x;
                position.y += velocityTrait.getTranslation().y;
                position.z += velocityTrait.getTranslation().z;

                trait.setRotX(trait.getRotX() + velocityTrait.getRotation().x);
                trait.setRotY(trait.getRotY() + velocityTrait.getRotation().y);
                trait.setRotZ(trait.getRotZ() + velocityTrait.getRotation().z);
            });
            worldPosition.transform(e, w -> Maths.applyTransformations(
                    positionTrait.getPosition(),
                    positionTrait.getRotX(),
                    positionTrait.getRotY(),
                    positionTrait.getRotZ(),
                    positionTrait.getScale(),
                    w.get(e).getTransformation()));
        });

        counter.operateAndLog();
    }
}

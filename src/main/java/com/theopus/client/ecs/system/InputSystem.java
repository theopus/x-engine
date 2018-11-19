package com.theopus.client.ecs.system;

import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.VelocityTrait;
import com.theopus.client.events.CustomTopics;
import com.theopus.client.events.MoveData;
import com.theopus.xengine.ecs.Ecs;
import com.theopus.xengine.ecs.mapper.WriteTraitMapper;
import com.theopus.xengine.ecs.system.EntitySystem;
import com.theopus.xengine.event.TopicReader;
import com.theopus.xengine.inject.Event;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.utils.JsonUtils;
import org.joml.Vector3f;

import java.util.BitSet;

public class InputSystem extends EntitySystem {

    @Event(topicId = CustomTopics.MOVE, listener = true)
    private TopicReader<MoveData> moveData;

    @Ecs
    private WriteTraitMapper<VelocityTrait> velocity;

    @Inject
    public InputSystem() {
        super(Context.WORK, false, 120);
    }

    @Override
    public void process(BitSet entities) {
        moveData.read().forEach(mde -> {
                VelocityTrait trait = velocity.get(1);
                MoveData data = mde.data();
                applyMove(0,trait, data);
        });
    }

    private void applyMove(int entity ,VelocityTrait trait, MoveData data) {
        velocity.transform(entity, wrapper -> {
            VelocityTrait vt = wrapper.get(entity);

            Vector3f rotation = vt.getRotation();
            Vector3f translation = vt.getTranslation();
            if (data.rotZ() != -1) {
                if (data.rotZ() == 1) {
                    rotation.z = 0.1f;
                } else {
                    rotation.z = 0f;
                }
            }
            if (data.left() != -1) {
                if (data.left() == 1) {
                    translation.x = -0.1f;
                } else {
                    translation.x = 0f;
                }
            }
            if (data.right() != -1) {
                if (data.right() == 1) {
                    translation.x = 0.1f;
                } else {
                    translation.x = 0f;
                }
            }
            if (data.top() != -1) {
                if (data.top() == 1) {
                    translation.y = 0.1f;
                } else {
                    translation.y = 0f;
                }
            }
            if (data.bot() != -1) {
                if (data.bot() == 1) {
                    translation.y = -0.1f;
                } else {
                    translation.y = 0f;
                }
            }
            if (data.forward() != -1) {
                if (data.forward() == 1) {
                    translation.z = 0.1f;
                } else {
                    translation.z = 0f;
                }
            }
            if (data.back() != -1) {
                if (data.back() == 1) {
                    translation.z = -0.1f;
                } else {
                    translation.z = 0f;
                }
            }

            if (data.rotY() != -1) {
                if (data.rotY() == 1) {
                    rotation.y = (float) Math.toRadians(180);
                } else {
                    rotation.y = 0f;
                }
            }
        });
    }
}

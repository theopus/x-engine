package com.theopus.xengine.core.ecs.systems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.managers.TagManager;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.XEngine;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.events.Subscriber;
import com.theopus.xengine.core.input.InputAction;
import com.theopus.xengine.core.input.InputActionType;
import com.theopus.xengine.core.input.InputEvent;
import com.theopus.xengine.core.utils.OpsCounter;

public class MoveSystem extends IntervalIteratingSystem implements Subscriber<InputEvent> {
    private static final Logger LOGGER = LoggerFactory.getLogger(MoveSystem.class);
    private final OpsCounter counter = new OpsCounter("Move");
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Transformation> mPosition;
    private TagManager tagManager;
    private ComponentMapper<Camera> mCamera;

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

    @Override
    public void onEvent(InputEvent inputEvent) {
        int entityId = tagManager.getEntityId(XEngine.MAIN_CAMERA);
        Velocity cameraVelocity = mVelocity.get(mCamera.get(entityId).target);
        float speed = 0.1f;
        InputAction action = inputEvent.action;
        boolean b = inputEvent.type == InputActionType.END;

        if (!b) {
            switch (action) {
                case UP:
                    cameraVelocity.position.y += speed;
                    break;
                case DOWN:
                    cameraVelocity.position.y -= speed;
                    break;
                case LEFT:
                    cameraVelocity.position.x -= speed;
                    break;
                case FORWARD:
                    cameraVelocity.position.z -= speed;
                    break;
                case BACK:
                    cameraVelocity.position.z += speed;
                    break;
                case RIGHT:
                    cameraVelocity.position.x += speed;
                    break;
                case ROTATE_CW:
                    cameraVelocity.rotation.y -= speed * 30;
                    break;
                case ROTATE_CCW:
                    cameraVelocity.rotation.y += speed * 30;
                    break;
                case UNIDENTIFIED:
                    break;
            }
        } else {
            switch (action) {
                case UP:
                    cameraVelocity.position.y = 0;
                    break;
                case DOWN:
                    cameraVelocity.position.y = 0;
                    break;
                case LEFT:
                    cameraVelocity.position.x = 0;
                    break;
                case RIGHT:
                    cameraVelocity.position.x = 0;
                    break;
                case FORWARD:
                    cameraVelocity.position.z = 0;
                    break;
                case BACK:
                    cameraVelocity.position.z = 0;
                    break;
                case ROTATE_CW:
                    cameraVelocity.rotation.y = 0;
                    break;
                case ROTATE_CCW:
                    cameraVelocity.rotation.y = 0;
                    break;
                case UNIDENTIFIED:
                    break;
            }
        }
    }
}

package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.IntervalIteratingSystem;
import com.artemis.systems.IntervalSystem;
import com.artemis.utils.ImmutableBag;
import com.theopus.xengine.core.XEngine;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.events.Subscriber;
import com.theopus.xengine.core.input.InputActionType;
import com.theopus.xengine.core.input.InputEvent;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.utils.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class CameraSystem extends IntervalIteratingSystem implements Subscriber<InputEvent> {

    private ComponentMapper<Position> pMapper;
    private ComponentMapper<Velocity> vMapper;
    private ComponentMapper<Camera> cMapper;
    private TagManager tagManager;
    @Wire(name = "renderer")
    private BaseRenderer renderer;

    public CameraSystem() {
        super(Aspect.all(Camera.class), 1);
    }

    @Override
    protected void process(int entityId) {
        Position position = getCameraPosition(entityId);
        Vector3f cameraposition = new Vector3f();
        position.position.negate(cameraposition);
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(cameraposition, position.rotation, position.scale);

        renderer.loadViewMatrix(transformationMatrix);
    }

    private Position getCameraPosition(int cameraId){
        Camera camera = cMapper.get(cameraId);
        return pMapper.get(camera.target);
    }

    private Velocity getCameraVelocity(int cameraId){
        Camera camera = cMapper.get(cameraId);
        return vMapper.get(camera.target);
    }

    @Override
    public void onEvent(InputEvent inputEvent) {
        int entityId = tagManager.getEntityId(XEngine.MAIN_CAMERA);
        Velocity cameraVelocity = getCameraVelocity(entityId);
        if (inputEvent.type == InputActionType.BEGIN){
            cameraVelocity.position.x = 0.01f;
        } else {
            cameraVelocity.position.x = 0f;
        }
    }
}

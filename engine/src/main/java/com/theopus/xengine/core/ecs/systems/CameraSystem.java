package com.theopus.xengine.core.ecs.systems;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.XEngine;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.events.Subscriber;
import com.theopus.xengine.core.input.InputAction;
import com.theopus.xengine.core.input.InputActionType;
import com.theopus.xengine.core.input.InputCursorEvent;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.utils.Maths;

public class CameraSystem extends IntervalIteratingSystem implements Subscriber<InputCursorEvent> {

    @Wire(name = "renderer")
    private BaseRenderer renderer;
    private ComponentMapper<Transformation> mTransformation;
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Camera> mCamera;
    private TagManager tagManager;

    public CameraSystem() {
        super(Aspect.all(Camera.class), 10);
    }

    public void move(Vector3f tpos, Vector3f trot) {
        Transformation transformation = getCameraTransformation();
        Camera camera = getCamera();

        float horiz = (float) (camera.distance * Math.cos(Math.toRadians(transformation.rotation.x)));
        float vert = (float) (camera.distance * Math.sin(Math.toRadians(transformation.rotation.x)));

        float theta = camera.angleAround;
//        float theta = trot.y + camera.angleAround;
        float offsetX = (float) (horiz * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horiz * Math.cos(Math.toRadians(theta)));

        transformation.position.x = tpos.x - offsetX;
        transformation.position.y = tpos.y + vert + camera.yOffset;
        transformation.position.z = tpos.z - offsetZ;

        transformation.rotation.y = 180 - (camera.angleAround);
//        transformation.rotation.y = 180 - (trot.y + camera.angleAround);
    }

    @Override
    protected void process(int entityId) {
        Transformation targetTransformation = getCameraTargetPosition(entityId);
        Transformation cTransformation = getCameraTransformation();

        move(targetTransformation.position, targetTransformation.rotation);

        Vector3f cameraposition = cTransformation.position.negate();
        Matrix4f transformationMatrix = Maths
                .createTransformationMatrix(cameraposition, cTransformation.rotation, cTransformation.scale)
                .m30(0f)
                .m31(0f)
                .m32(0f)
                .translate(cameraposition);

        renderer.loadViewMatrix(transformationMatrix);
    }

    private Transformation getCameraTargetPosition(int cameraId) {
        Camera camera = mCamera.get(cameraId);
        return mTransformation.get(camera.target);
    }

    @Override
    public void onEvent(InputCursorEvent inputCursorEvent) {
        if (inputCursorEvent.stateOf(InputAction.ACTION0) == InputActionType.BEGIN) {
            getCameraTransformation().rotation.x += inputCursorEvent.dy * 0.4f;
            getCamera().angleAround -= inputCursorEvent.dx * 0.4f;
        }

        if (inputCursorEvent.stateOf(InputAction.ACTION1) == InputActionType.BEGIN) {
            getCamera().distance += inputCursorEvent.dy * 0.2f;
        }
    }

    private Transformation getCameraTransformation() {
        return mTransformation.get(getCameraId());
    }

    private Camera getCamera() {
        return mCamera.get(getCameraId());
    }

    private int getCameraId() {
        return tagManager.getEntityId(XEngine.MAIN_CAMERA);
    }
}

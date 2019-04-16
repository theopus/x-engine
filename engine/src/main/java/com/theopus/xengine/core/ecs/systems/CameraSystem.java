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

public class CameraSystem extends IntervalIteratingSystem implements Subscriber<InputCursorEvent>{

    private ComponentMapper<Transformation> mTransformation;
    private ComponentMapper<Velocity> mVelocity;
    private ComponentMapper<Camera> mCamera;
    private TagManager tagManager;
    @Wire(name = "renderer")
    private BaseRenderer renderer;

    public CameraSystem() {
        super(Aspect.all(Camera.class), 10);
    }


    private float calculateHorizontal(Vector3f rot) {
        return (float) (getCamera().distance * Math.cos(Math.toRadians(rot.x)));
    }

    private float calculateVertical(Vector3f rot) {
        return (float) (getCamera().distance * Math.sin(Math.toRadians(rot.x)));
    }

    public void move(Vector3f tpos, Vector3f trot, Transformation cameraPosition) {
        calculateCameraPos(tpos, trot, calculateHorizontal(cameraPosition.rotation), calculateVertical(cameraPosition.rotation), cameraPosition);
        cameraPosition.rotation.y = 180 - (trot.y + getCamera().angleAround);
    }

    private void calculateCameraPos(Vector3f pos, Vector3f rot, float horiz, float vert, Transformation cameraPosition) {
        Vector3f position = pos;
        float theta = rot.y + getCamera().angleAround;
        float offsetX = (float) (horiz * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horiz * Math.cos(Math.toRadians(theta)));


        cameraPosition.position.x = position.x - offsetX;
        cameraPosition.position.y = position.y + vert + getCamera().yOffset;
        cameraPosition.position.z = position.z - offsetZ;

    }

    @Override
    protected void process(int entityId) {
        Transformation targetTransformation = getCameraTargetPosition(entityId);
        Transformation cameraPosition = getCameraTransformation();

        move(targetTransformation.position, targetTransformation.rotation, cameraPosition);

        Vector3f cameraposition = cameraPosition.position.negate();
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(cameraposition, cameraPosition.rotation, cameraPosition.scale);
        transformationMatrix
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

    private Velocity getCameraTargetVelocity(int cameraId) {
        Camera camera = mCamera.get(cameraId);
        return mVelocity.get(camera.target);
    }


    @Override
    public void onEvent(InputCursorEvent inputCursorEvent) {
        if (inputCursorEvent.stateOf(InputAction.ACTION0) == InputActionType.BEGIN){
            getCameraTransformation().rotation.x += inputCursorEvent.dy * 0.4f;
            getCamera().angleAround -= inputCursorEvent.dx * 0.4f;
        }

        if (inputCursorEvent.stateOf(InputAction.ACTION1) == InputActionType.BEGIN){
            getCamera().distance += inputCursorEvent.dy * 0.2f;
        }
    }

    private Transformation getCameraTransformation(){
        return mTransformation.get(getCameraId());
    }

    private Camera getCamera(){
        return mCamera.get(getCameraId());
    }

    private int getCameraId() {
        return tagManager.getEntityId(XEngine.MAIN_CAMERA);
    }
}

package com.theopus.xengine.trait;

import org.joml.Vector3f;

public class PositionTrait extends Trait {

    private Vector3f position = new Vector3f();
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale = 1f;
    private float rotSpeed = 0.001f;

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getRotX() {
        return rotX;
    }

    public void setRotX(float rotX) {
        this.rotX = rotX;
    }

    public float getRotY() {
        return rotY;
    }

    public void setRotY(float rotY) {
        this.rotY = rotY;
    }

    public float getRotZ() {
        return rotZ;
    }

    public void setRotZ(float rotZ) {
        this.rotZ = rotZ;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void setRotSpeed(float rotSpeed) {
        this.rotSpeed = rotSpeed;
    }

    public float getRotSpeed() {
        return rotSpeed;
    }
}

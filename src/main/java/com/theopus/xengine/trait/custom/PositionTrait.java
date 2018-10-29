package com.theopus.xengine.trait.custom;

import com.theopus.xengine.trait.IDuplicate;
import com.theopus.xengine.trait.Trait;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionTrait extends Trait<PositionTrait> implements IDuplicate<PositionTrait> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PositionTrait.class);

    private Vector3f position = new Vector3f();
    private float rotX;
    private float rotY;
    private float rotZ;
    private float scale = 1f;
    private float rotSpeed = 0.000f;

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

    public float getRotSpeed() {
        return rotSpeed;
    }

    public void setRotSpeed(float rotSpeed) {
        this.rotSpeed = rotSpeed;
    }

    @Override
    public PositionTrait duplicateTo(PositionTrait trait) {
        trait.position.x = this.position.x;
        trait.position.y = this.position.y;
        trait.position.z = this.position.z;

        trait.rotX = this.rotX;
        trait.rotY = this.rotY;
        trait.rotZ = this.rotZ;

        trait.scale = this.scale;
        trait.rotSpeed = this.rotSpeed;

        trait.gen = this.gen;
        return trait;
    }

}

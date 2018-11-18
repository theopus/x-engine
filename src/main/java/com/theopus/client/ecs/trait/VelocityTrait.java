package com.theopus.client.ecs.trait;

import com.theopus.xengine.ecs.Trait;
import com.theopus.xengine.utils.CopyUtils;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class VelocityTrait extends Trait<VelocityTrait> {

    private Vector3f translation = new Vector3f();
    private Vector3f rotation = new Vector3f();

    public Vector3f getTranslation() {
        return translation;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setTranslation(Vector3f translation) {
        this.translation = translation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    @Override
    public VelocityTrait duplicateTo(VelocityTrait velocityTrait) {
        velocityTrait.gen = this.gen;
        CopyUtils.copy(this.rotation, velocityTrait.rotation);
        CopyUtils.copy(this.translation, velocityTrait.translation);
        return velocityTrait;
    }
}

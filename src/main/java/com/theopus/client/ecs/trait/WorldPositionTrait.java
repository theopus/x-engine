package com.theopus.client.ecs.trait;

import com.theopus.xengine.ecs.Trait;
import com.theopus.xengine.ecs.mapper.IDuplicate;
import com.theopus.xengine.utils.CopyUtils;
import org.joml.Matrix4f;

public class WorldPositionTrait extends Trait<WorldPositionTrait> implements IDuplicate<WorldPositionTrait> {

    private Matrix4f transformation = new Matrix4f().identity();

    public Matrix4f getTransformation() {
        return transformation;
    }

    public void setTransformation(Matrix4f transformation) {
        this.transformation = transformation;
    }

    @Override
    public WorldPositionTrait duplicateTo(WorldPositionTrait trait) {
        CopyUtils.copy(this.getTransformation(), trait.getTransformation());
        trait.gen = this.gen;
        return trait;
    }
}

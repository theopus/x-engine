package com.theopus.xengine.trait.custom;

import com.theopus.xengine.trait.IDuplicate;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.utils.CopyUtils;
import org.joml.Matrix4f;

public class RenderTrait extends Trait<RenderTrait> implements IDuplicate<RenderTrait> {

    private int vaoId;
    private int vertexCount;
    private Matrix4f transformation = new Matrix4f().identity();

    public int getVao() {
        return vaoId;
    }

    public int getVaoId() {
        return vaoId;
    }

    public void setVaoId(int vaoId) {
        this.vaoId = vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public Matrix4f getTransformation() {
        return transformation;
    }

    public void setTransformation(Matrix4f transformation) {
        this.transformation = transformation;
    }

    @Override
    public RenderTrait duplicateTo(RenderTrait trait) {
        trait.vaoId = this.vaoId;
        trait.vertexCount = this.vertexCount;
        CopyUtils.copy(this.getTransformation(), trait.getTransformation());
        trait.gen = this.gen;
        return trait;
    }
}

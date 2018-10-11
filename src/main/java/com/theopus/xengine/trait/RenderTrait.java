package com.theopus.xengine.trait;

import org.joml.Matrix4f;

public class RenderTrait implements Trait {

    private int vaoId;
    private int vertexCount;
    private Matrix4f transformation = new Matrix4f().identity();

    public int getVao() {
        return vaoId;
    }

    public int getVaoId() {
        return vaoId;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public Matrix4f getTransformation() {
        return transformation;
    }

    public void setVaoId(int vaoId) {
        this.vaoId = vaoId;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public void setTransformation(Matrix4f transformation) {
        this.transformation = transformation;
    }
}
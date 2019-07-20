package com.theopus.xengine.core.render.abstraction;

public interface VertexArray {
    void bind();
    void unbind();
    void setIndexBuffer(IndexBuffer indexBuffer);
    void addVertexBuffer(VertexBuffer vertexBuffer);

}

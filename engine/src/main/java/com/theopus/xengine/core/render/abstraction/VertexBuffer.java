package com.theopus.xengine.core.render.abstraction;

public interface VertexBuffer {
    void bind();
    void setLayout(BufferLayout layout);
    void unbind();
}

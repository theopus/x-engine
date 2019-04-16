package com.theopus.xengine.wrapper.opengl.shader.ubos;

import java.io.Closeable;

import com.theopus.xengine.wrapper.opengl.objects.Ubo;

public abstract class UniformBlock implements Closeable {
    protected final Ubo ubo;
    private final int bindingPoint;
    private final String name;
    private final int size;

    public UniformBlock(int bindingPoint, String name, Ubo ubo) {
        this.bindingPoint = bindingPoint;
        this.name = name;
        this.size = ubo.getSize();
        this.ubo = ubo;
    }

    public void bindToIndex() {
        ubo.bindToIndex(bindingPoint);
    }

    public int getBindingPoint() {
        return bindingPoint;
    }

    public String getName() {
        return name;
    }
}

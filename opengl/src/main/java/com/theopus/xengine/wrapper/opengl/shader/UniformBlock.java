package com.theopus.xengine.wrapper.opengl.shader;

import com.theopus.xengine.wrapper.opengl.objects.Ubo;

import java.io.Closeable;

public abstract class UniformBlock implements Closeable {
    private final int bindingPoint;
    private final String name;
    private final int size;
    protected final Ubo ubo;

    public UniformBlock(int bindingPoint, String name, Ubo ubo) {
        this.bindingPoint = bindingPoint;
        this.name = name;
        this.size = ubo.getSize();
        this.ubo = ubo;
    }

    public void bindToIndex(){
        ubo.bindToIndex(bindingPoint);
    }

    public int getBindingPoint() {
        return bindingPoint;
    }

    public String getName() {
        return name;
    }
}

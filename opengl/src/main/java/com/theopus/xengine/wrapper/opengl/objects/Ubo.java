package com.theopus.xengine.wrapper.opengl.objects;

import org.lwjgl.opengl.GL31;

public class Ubo extends GlBuffer {
    private final static int type = GL31.GL_UNIFORM_BUFFER;

    public Ubo(int size, int usage) {
        super(size, type, usage);
    }

    public void bindToIndex(int index){
        GL31.glBindBufferRange(type, index, id, 0, size);
    }
}

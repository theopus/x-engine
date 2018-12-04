package com.theopus.xengine.wrapper.opengl.buffers;

import com.theopus.xengine.wrapper.opengl.buffers.GlBuffer;
import com.theopus.xengine.wrapper.opengl.utils.GlToString;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import java.nio.FloatBuffer;

public class Ubo extends GlBuffer {
    private final static int type = GL31.GL_UNIFORM_BUFFER;

    public Ubo(int size, int usage) {
        super(size, type, usage);
    }

    public void bindToIndex(int index){
        GL31.glBindBufferRange(type, index, id, 0, size);
    }
}

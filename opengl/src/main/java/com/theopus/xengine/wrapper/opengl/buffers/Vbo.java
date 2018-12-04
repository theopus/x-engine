package com.theopus.xengine.wrapper.opengl.buffers;

import org.lwjgl.opengl.GL15;

public class Vbo extends GlBuffer {

    private static int type = GL15.GL_ARRAY_BUFFER;

    public Vbo(int[] data, int usage) {
        super(data, type, usage);
    }

    public Vbo(float[] data, int usage) {
        super(data, type, usage);
    }
}

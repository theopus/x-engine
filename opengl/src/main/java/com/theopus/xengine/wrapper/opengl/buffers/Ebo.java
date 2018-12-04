package com.theopus.xengine.wrapper.opengl.buffers;

import org.lwjgl.opengl.GL15;

public class Ebo extends GlBuffer {
    private static int type = GL15.GL_ELEMENT_ARRAY_BUFFER;
    protected final int lengths;

    public Ebo(int[] data, int usage) {
        super(data, type, usage);
        this.lengths = data.length;
    }
}

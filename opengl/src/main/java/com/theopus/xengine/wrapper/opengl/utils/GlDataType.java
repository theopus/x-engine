package com.theopus.xengine.wrapper.opengl.utils;

import org.lwjgl.opengl.GL15;

public enum GlDataType {

    FLOAT(4, GL15.GL_FLOAT, 1),
    VEC2_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 2),
    VEC3_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 3),
    VEC4_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 4);

    public final int bytes;
    public final int glType;
    public final int size;

    GlDataType(int bytes, int glType, int size) {
        this.bytes = bytes;
        this.glType = glType;
        this.size = size;
    }

    public int byteSize(){
        return bytes * size;
    }

}

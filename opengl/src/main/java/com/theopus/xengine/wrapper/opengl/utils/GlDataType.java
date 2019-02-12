package com.theopus.xengine.wrapper.opengl.utils;

import org.lwjgl.opengl.GL15;

public enum GlDataType {

    FLOAT(4, GL15.GL_FLOAT, 1, 4),
    VEC2_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 2, 8),
    VEC3_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 3, 12),
    VEC4_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 4, 16),
    MAT4_FLOAT(FLOAT.bytes, GL15.GL_FLOAT, 16, 64);

    public final int bytes;
    public final int glType;
    public final int size;
    public final int byteSize;

    GlDataType(int bytes, int glType, int size, int byteSize) {
        this.bytes = bytes;
        this.glType = glType;
        this.size = size;
        this.byteSize = byteSize;
    }
}

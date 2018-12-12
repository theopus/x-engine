package com.theopus.xengine.wrapper.opengl.utils;

import org.joml.Matrix4f;

import java.nio.FloatBuffer;

public class Buffers {
    /**
     * limit is crucial since limit value used for determining number of values
     */
    public static void put(Matrix4f src, FloatBuffer dest) {
        src.get(dest);
        dest.limit(16);
    }
}
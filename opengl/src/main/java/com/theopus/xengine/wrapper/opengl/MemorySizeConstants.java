package com.theopus.xengine.wrapper.opengl;

import org.lwjgl.opengl.GL15;

public class MemorySizeConstants {

    public static final int BYTE = 1;
    public static final int FLOAT = BYTE * 4;
    public static final int INT = BYTE * 4;
    public static final int VEC3_FLOAT = FLOAT * 3;
    public static final int VEC4_FLOAT = FLOAT * 4;
    public static final int VEC3_INT = FLOAT * 3;
    public static final int MAT4_FLOAT = VEC4_FLOAT * 4;
    public static final int VEC2_FLOAT = FLOAT * 2;

    public static int typeSize(int glType){
        switch (glType){
            case GL15.GL_FLOAT:
                return FLOAT;
            default:
                return -1;
        }
    }
}

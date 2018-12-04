package com.theopus.xengine.wrapper.opengl.utils;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL31;

public class GlToString {

    public static String dataType(int type){
        switch (type){
            case GL15.GL_FLOAT: return "FLOAT";
            case GL15.GL_INT: return "INT";
            default: return "UNIDENTIFIED";
        }
    }

    public static String bufferType(int type){
        switch (type){
            case GL15.GL_ARRAY_BUFFER: return "ARRAY_BUFFER";
            case GL31.GL_UNIFORM_BUFFER: return "UNIFORM_BUFFER";
            case GL31.GL_ELEMENT_ARRAY_BUFFER: return "ELEMENT_ARRAY_BUFFER";
            default: return "UNIDENTIFIED";
        }
    }


    public static String bufferUsage(int usage){
        switch (usage){
            case GL15.GL_STATIC_DRAW: return "STATIC_DRAW";
            default: return "UNIDENTIFIED";
        }
    }
}

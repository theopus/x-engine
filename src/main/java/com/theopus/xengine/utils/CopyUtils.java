package com.theopus.xengine.utils;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.internal.MemUtil;

public class CopyUtils {

    private static MemUtil cpy = MemUtil.INSTANCE;


    public static void copy(Matrix4f from, Matrix4f to){
        cpy.copy(from, to);
    }

    public static void copy(Vector3f from, Vector3f to){
        to.x = from.x;
        to.y = from.y;
        to.z = from.z;
    }
}

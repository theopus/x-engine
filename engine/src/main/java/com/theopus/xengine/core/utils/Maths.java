package com.theopus.xengine.core.utils;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class Maths {

    public static Matrix4f createTransformationMatrix2D(Vector2f translation, Vector2f scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(translation.x, translation.y, 0);
        matrix.scale(new Vector3f(scale.x, scale.y, 1f));
        return matrix;
    }

    public static Matrix4f createTransformationMatrix(Vector3f vector3f, float rx, float ry, float rz, float scale) {
        return new Matrix4f()
                .identity()
                .setTranslation(vector3f)
                .rotate(((float) Math.toRadians(rx)), new Vector3f(1, 0, 0))
                .rotate(((float) Math.toRadians(ry)), new Vector3f(0, 1, 0))
                .rotate(((float) Math.toRadians(rz)), new Vector3f(0, 0, 1))
                .scale(scale);
    }
    public static Matrix4f createTransformationMatrix(Vector3f vector3f, Vector3f rotation, float scale) {
        return createTransformationMatrix(vector3f, rotation.x, rotation.y, rotation.z, scale);
    }

    public static Matrix4f applyTransformations(Vector3f vector3f, float rx, float ry, float rz, float scale, Matrix4f matrix4f) {
        return matrix4f
                .identity()
                .setTranslation(vector3f)
                .rotate(((float) Math.toRadians(rx)), new Vector3f(1, 0, 0))
                .rotate(((float) Math.toRadians(ry)), new Vector3f(0, 1, 0))
                .rotate(((float) Math.toRadians(rz)), new Vector3f(0, 0, 1))
                .scale(scale);
    }

    public static Matrix4f createProjectionMatrix(float FOV, float near, float far, int windowWidth, int windowHeight) {
        Matrix4f result = new Matrix4f().identity();
        float aspectRatio = (float) windowWidth / (float) windowHeight;
        float yScale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustrumLenght = far - near;

        result.m00(xScale);
        result.m11(yScale);
        result.m22(-((far + near) / frustrumLenght));
        result.m23(-1);
        result.m32(-((2 * near * far) / frustrumLenght));
        result.m33(0);
        return result;
    }

    public static Matrix4f createProjectionMatrixAuto(float fov, float near, float far, float aspectRatio) {
        return new Matrix4f().perspective(fov, aspectRatio, near, far);
    }
}

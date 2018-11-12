package com.theopus.xengine.opengl.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

public class Uniform<T> {

    protected String name;
    protected int location;
    private LoadFunction<T> function;

    public Uniform(String name, LoadFunction<T> function) {
        this.name = name;
        this.function = function;
    }

    private Uniform(String name) {
        this.name = name;
    }

    public static Uniform<Vector3f> ofVec3(String name) {
        return new Uniform<Vector3f>(name) {
            @Override
            public void load(Vector3f value) {
                GL20.glUniform3f(location, value.x, value.y, value.z);
            }
        };
    }

    public static Uniform<Float> ofFloat(String name) {
        return new Uniform<Float>(name) {
            @Override
            public void load(Float value) {
                GL20.glUniform1f(location, value);
            }
        };
    }

    public static Uniform<Integer> ofInt(String name) {
        return new Uniform<Integer>(name) {
            @Override
            public void load(Integer value) {
                GL20.glUniform1i(location, value);
            }
        };
    }

    public static Uniform<Boolean> ofBool(String name) {
        return new Uniform<Boolean>(name) {
            @Override
            public void load(Boolean value) {
                GL20.glUniform1f(location, value ? 1 : 0);
            }
        };
    }

    public static Uniform<Matrix4f> ofMatrix4f(String name) {
        return new Uniform<Matrix4f>(name) {

            private FloatBuffer matrixBuffer;

            @Override
            public void prepare(ShaderProgram program) {
                super.prepare(program);
                this.matrixBuffer = MemoryUtil.memAllocFloat(16);
            }

            @Override
            public void load(Matrix4f value) {
                matrixBuffer.clear();
                FloatBuffer floatBuffer = value.get(matrixBuffer);
                GL20.glUniformMatrix4fv(location, false, floatBuffer);
                matrixBuffer.clear();
            }

            @Override
            void close() {
                super.close();
                MemoryUtil.memFree(matrixBuffer);
            }
        };
    }

    void prepare(ShaderProgram program) {
        GL20.glGetUniformLocation(program.getId(), name);
    }

    public void load(T t) {
        function.load(t);
    }

    void close() {

    }

    public interface LoadFunction<V> {
        void load(V v);
    }

}

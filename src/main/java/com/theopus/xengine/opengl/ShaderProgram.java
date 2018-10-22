package com.theopus.xengine.opengl;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;

    private final FloatBuffer matrixBuffer;

    private static Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);

    public ShaderProgram(int vertexShaderID, int fragmentShaderID) {
        this.vertexShaderID = vertexShaderID;
        this.fragmentShaderID = fragmentShaderID;
        this.programID = GL20.glCreateProgram();
        this.matrixBuffer = MemoryUtil.memAllocFloat(16);

        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAllAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    public ShaderProgram(String vertexFile, String fragmentFile) throws IOException {
        this(
                loadShader(vertexFile, Type.VERTEX),
                loadShader(fragmentFile, Type.FRAGMENT)
        );
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        int i = GL20.glGetUniformLocation(programID, uniformName);
        LOGGER.info("Uniform {} to {}", uniformName, i);
        return i;
    }

    protected int getStructUniformLocation(String structName, String uniformName) {
        int i = GL20.glGetUniformLocation(programID, structName + uniformName);
        LOGGER.info("Struct uniform {} to {}", structName + uniformName, i);
        return i;
    }

    protected abstract void bindAllAttributes();

    public void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    public void loadInt(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    public void loadVector3f(int location, Vector3f value) {
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    public void loadBool(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1 : 0);
    }

    public void loadMatrix4f(int location, Matrix4f value) {
        matrixBuffer.clear();
        FloatBuffer floatBuffer = value.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, floatBuffer);
        matrixBuffer.clear();
    }

    protected void bindAttribute(int position, String placeholder) {
        GL20.glBindAttribLocation(programID, position, placeholder);
    }

    public void bind() {
        GL20.glUseProgram(programID);
    }

    public void unbind() {
        GL20.glUseProgram(0);
    }

    public void cleanup() {
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
        MemoryUtil.memFree(matrixBuffer);
    }

    public static int loadShader(String file, Type type) throws IOException {
        StringBuilder shaderSource = fileToStringBuilder(file);

        LOGGER.info("SHADER = \n{}", shaderSource);
        int shaderID = GL20.glCreateShader(type.binding());
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException(String.format("Exception during initializing of [%s] shader, shader file: [%s] OpenGL: ", type, file) +
                    GL20.glGetShaderInfoLog(shaderID, 10_000));

        }
        return shaderID;
    }

    private static StringBuilder fileToStringBuilder(String file) throws IOException {
        StringBuilder shaderSource = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(ShaderProgram.class.getClassLoader().getResourceAsStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                shaderSource.append(line).append('\n');
            }
        }
        return shaderSource;
    }


    public enum Type {
        VERTEX(GL20.GL_VERTEX_SHADER),
        FRAGMENT(GL20.GL_FRAGMENT_SHADER),;
        private int glBinding;

        Type(int glBinding) {
            this.glBinding = glBinding;
        }

        public int binding() {
            return this.glBinding;
        }
    }


    public static final class Uniforms {
        public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
        public static final String PROJECTION_MATRIX = "projectionMatrix";
        public static final String VIEW_MATRIX = "viewMatrix";
        public static final String LIGHT_POSITION = "lightPosition";
        public static final String LIGHT_COLOR = "lightColor";


        public static class Material {
            public static final String VARIABLE = "mat";
            public static final String HAS_TEXTURE = ".hasTexture";
            public static final String REFLECTIVITY = ".reflectivity";
            public static final String SHINE_DAMPER = ".shineDamper";
            public static final String HAS_TRANSPARENCY = ".hasTransparency";
            public static final String USE_FAKE_LIGHT = ".useFakeLight";
        }

        public static class Fog {
            public static final String VARIABLE = "fog";
            public static final String ENABLED = ".enabled";
            public static final String COLOR = ".color";
            public static final String DESITY = ".density";
            public static final String GRADIENT = ".gradient";
        }


        public static class BlendTextures {
            public static final String BLEND_MAP = "blendMapTexture";
            public static final String BACKGROUND = "bgTexture";
            public static final String R = "rTexture";
            public static final String G = "gTexture";
            public static final String B = "bTexture";
        }
    }
}

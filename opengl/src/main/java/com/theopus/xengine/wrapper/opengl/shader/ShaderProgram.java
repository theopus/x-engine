package com.theopus.xengine.wrapper.opengl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public abstract class ShaderProgram {

    private static Logger LOGGER = LoggerFactory.getLogger(ShaderProgram.class);
    private final int programID;
    private final int vertexShaderID;
    private final int fragmentShaderID;
    private final FloatBuffer matrixBuffer;

    protected List<Uniform<?>> uniforms = new ArrayList<>();

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

        this.uniforms = uniforms();
        this.uniforms.forEach(uniform -> uniform.prepare(this));
    }


    public ShaderProgram(String vertexFile, String fragmentFile) throws IOException {
        this(
                loadShader(vertexFile, Type.VERTEX),
                loadShader(fragmentFile, Type.FRAGMENT)
        );
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

    protected abstract List<Uniform<?>> uniforms();

    protected abstract void bindAllAttributes();

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
        uniforms.forEach(Uniform::close);
    }

    public int getId() {
        return programID;
    }


    public enum Type {
        VERTEX(GL20.GL_VERTEX_SHADER),
        FRAGMENT(GL20.GL_FRAGMENT_SHADER),
        ;
        private int glBinding;

        Type(int glBinding) {
            this.glBinding = glBinding;
        }

        public int binding() {
            return this.glBinding;
        }
    }


}

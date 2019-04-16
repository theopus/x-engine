package com.theopus.xengine.wrapper.opengl.shader;

import java.util.Arrays;
import java.util.List;

import org.joml.Matrix4f;

public class StaticShader extends ShaderProgram {
    private Uniform<Matrix4f> transformation;

    public StaticShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected List<Uniform<?>> uniforms() {
        transformation = Uniform.ofMatrix4f(Uniforms.TRANSFORMATION_MATRIX);
        return Arrays.asList(transformation);
    }

    @Override
    protected void bindAllAttributes() {

    }


    public Uniform<Matrix4f> transformation() {
        return transformation;
    }

}


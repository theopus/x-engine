package com.theopus.xengine.wrapper.opengl.shader;

import org.joml.Matrix4f;

import java.util.Arrays;
import java.util.List;

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


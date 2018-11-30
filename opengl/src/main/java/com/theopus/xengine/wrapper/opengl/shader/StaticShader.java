package com.theopus.xengine.wrapper.opengl.shader;

import org.joml.Matrix4f;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StaticShader extends ShaderProgram {
    private Uniform<Matrix4f> transformation;
    private Uniform<Matrix4f> projection;
    private Uniform<Matrix4f> view;

    public StaticShader(String vertexFile, String fragmentFile){
        super(vertexFile, fragmentFile);
    }

    @Override
    protected List<Uniform<?>> uniforms() {
        transformation = Uniform.ofMatrix4f(Uniforms.TRANSFORMATION_MATRIX);
        projection = Uniform.ofMatrix4f(Uniforms.PROJECTION_MATRIX);
        view = Uniform.ofMatrix4f(Uniforms.VIEW_MATRIX);

        return Arrays.asList(transformation, projection, view);
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uv");
        super.bindAttribute(2, "normal");
    }


    public Uniform<Matrix4f> transformation() {
        return transformation;
    }

    public Uniform<Matrix4f> projection() {
        return projection;
    }

    public Uniform<Matrix4f> view() {
        return view;
    }
}


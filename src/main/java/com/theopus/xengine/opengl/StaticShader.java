package com.theopus.xengine.opengl;

import org.joml.Matrix4f;

import java.io.IOException;

public class StaticShader extends ShaderProgram {

    private int transMatrixLocation;
    private int projMatrixLocation;
    private int viewMatrixLocation;

    public StaticShader(int programID, int vertexShaderID) {
        super(programID, vertexShaderID);
    }

    public StaticShader(String vertexFile, String fragmentFile) throws IOException {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected void getAllUniformLocations() {
        transMatrixLocation = super.getUniformLocation(Uniforms.TRANSFORMATION_MATRIX);
        projMatrixLocation = super.getUniformLocation(Uniforms.PROJECTION_MATRIX);
        viewMatrixLocation = super.getUniformLocation(Uniforms.VIEW_MATRIX);
    }

    @Override
    protected void bindAllAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "uv");
        super.bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix4f) {
        super.loadMatrix4f(transMatrixLocation, matrix4f);
    }

    public void loadProjectionMatrix(Matrix4f projMatrix) {
        super.loadMatrix4f(projMatrixLocation, projMatrix);
    }

    public void loadViewMatrix(Matrix4f viewMatrix) {
        super.loadMatrix4f(viewMatrixLocation, viewMatrix);
    }

}

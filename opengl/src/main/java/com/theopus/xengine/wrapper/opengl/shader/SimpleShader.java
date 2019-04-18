package com.theopus.xengine.wrapper.opengl.shader;

import java.util.Collections;
import java.util.List;

public class SimpleShader extends ShaderProgram {
    public SimpleShader(int vertexShaderID, int fragmentShaderID) {
        super(vertexShaderID, fragmentShaderID);
    }

    public SimpleShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected List<Uniform<?>> uniforms() {
        return Collections.emptyList();
    }

    @Override
    protected void bindAllAttributes() {

    }
}

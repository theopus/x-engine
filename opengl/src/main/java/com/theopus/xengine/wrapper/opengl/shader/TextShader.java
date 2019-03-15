package com.theopus.xengine.wrapper.opengl.shader;

import org.joml.Vector2f;

import java.util.Arrays;
import java.util.List;

public class TextShader extends ShaderProgram{

    private Uniform<Vector2f> location = Uniform.ofVec2("location");

    public TextShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }

    @Override
    protected List<Uniform<?>> uniforms() {
        return Arrays.asList(location);
    }

    @Override
    protected void bindAllAttributes() {

    }
}

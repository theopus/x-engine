package com.theopus.xengine.core.render.gl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import com.theopus.xengine.wrapper.opengl.shader.ShaderProgram;
import com.theopus.xengine.wrapper.opengl.shader.Uniform;

/*

 */
public class FontShader extends ShaderProgram {
    private Uniform<Vector2f> transformation;
    private Uniform<Vector3f> color;

    public FontShader(String vertexFile, String fragmentFile) {
        super(vertexFile, fragmentFile);
    }


    @Override
    protected List<Uniform<?>> uniforms() {
        transformation = Uniform.ofVec2("transformation");
        color = Uniform.ofVec3("text_color");
        return Arrays.asList(transformation, color);
    }

    @Override
    protected void bindAllAttributes() {

    }

    public Uniform<Vector2f> transformation() {
        return transformation;
    }

    public Uniform<Vector3f> color() {
        return color;
    }
}


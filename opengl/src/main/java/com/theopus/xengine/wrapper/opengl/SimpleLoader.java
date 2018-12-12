package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.opengl.objects.*;
import org.lwjgl.opengl.GL15;

public class SimpleLoader extends Loader {

    public SimpleLoader(MemoryContext context) {
        super(context);
    }

    public Vao load(float[] positions, int[] indexes) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);
        Vbo vbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Attribute posAttr = Attribute
                .singleVboAttribute(0, GL15.GL_FLOAT, vbo, 3, MemorySizeConstants.FLOAT);

        context.put(ebo, vbo);
        return new Vao(ebo, posAttr);
    }

    public TexturedVao load(float[] positions, float[] uvs, int[] indexes, String path) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);

        Vbo pos = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Vbo uv = new Vbo(uvs, GL15.GL_STATIC_DRAW);

        Attribute posAttr = Attribute
                .singleVboAttribute(0, GL15.GL_FLOAT, pos, 3, MemorySizeConstants.FLOAT);

        Attribute uvAttr = Attribute
                .singleVboAttribute(1, GL15.GL_FLOAT, uv, 2, MemorySizeConstants.FLOAT);

        Vao vao = new Vao(ebo, posAttr, uvAttr);
        Texture texture = loadTexture(path);

        context.put(ebo, pos, uv);
        context.put(texture);
        return new TexturedVao(vao, texture);

    }


}

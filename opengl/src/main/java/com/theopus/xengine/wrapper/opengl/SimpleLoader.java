package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.opengl.objects.Attribute;
import com.theopus.xengine.wrapper.opengl.objects.Ebo;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.objects.Vbo;
import org.lwjgl.opengl.GL15;

public class SimpleLoader extends Loader {

    public SimpleLoader(MemoryContext context) {
        super(context);
    }

    public Vao loadSimpleVao(float[] positions, int[] indexes) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);
        Vbo vbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Attribute posAttr = Attribute
                .singleVboAttribute(0, GL15.GL_FLOAT, vbo, 3, MemorySizeConstants.FLOAT);

        return new Vao(ebo, posAttr);
    }


}

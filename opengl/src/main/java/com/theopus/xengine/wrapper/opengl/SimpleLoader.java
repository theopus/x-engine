package com.theopus.xengine.wrapper.opengl;

import org.lwjgl.assimp.AIMaterial;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

public class SimpleLoader extends Loader {
    public Vao loadSimpleVao(float[] positions, int[] indexes) {
        int vao = createVAO();
        bindVao(vao);
        int eboId = bindIndicesBuffer(indexes);
        int posVbo = writeInVao(0, 3, positions);
        //
        GL30.glEnableVertexAttribArray(0);
        unbindVao();
        unbindIndicesBuffer();

        Vbo vbo = new Vbo(posVbo, GL15.GL_ARRAY_BUFFER, MemorySizeConstants.VEC3_FLOAT * positions.length, GL15.GL_STATIC_DRAW);
        Vbo ebo = new Vbo(eboId, GL15.GL_ELEMENT_ARRAY_BUFFER, MemorySizeConstants.INT * indexes.length, GL15.GL_STATIC_DRAW);

        Attribute posAttr = new Attribute("positions", 0, 3);


        return new VaoBuilder()
                .setAttributes(posAttr)
                .setId(vao)
                .setVertexes(indexes.length)
                .setVbos(vbo)
                .setEbo(ebo)
                .createVao();
    }
}

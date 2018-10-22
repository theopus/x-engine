package com.theopus.xengine.opengl;

import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.utils.MemorySizeConstants;
import org.lwjgl.opengl.GL15;

public class RenderTraitLoader extends Loader {

    public RenderTrait loadEntity(RenderTrait trait, float[] positions, int vertexCount) {
        int vao = createVAO();
        bindVao(vao);
        writeInVao(0, 3, positions);
        unbindVao();

        trait.setVaoId(vao);
        trait.setVertexCount(vertexCount);
        return trait;
    }

    public Vao loadSimpleVao(float[] positions, int vertexCount) {
        int vao = createVAO();
        bindVao(vao);
        int posVbo = writeInVao(0, 3, positions);
        unbindVao();
        
        Vbo vbo = new Vbo(posVbo, GL15.GL_ARRAY_BUFFER, MemorySizeConstants.VEC3_FLOAT * positions.length, GL15.GL_STATIC_DRAW);
        Attribute posAttr = new Attribute("positions", 0, 3);


        return new VaoBuilder()
                .setAttributes(posAttr)
                .setId(vao)
                .setVbos(vbo)
                .setEbo(null)
                .createVao();
    }

}

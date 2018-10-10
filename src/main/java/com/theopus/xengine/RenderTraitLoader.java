package com.theopus.xengine;

import com.theopus.xengine.trait.RenderTrait;

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

}

package com.theopus.xengine;

public class EntityLoader extends Loader {

    public Entity loadEntity(float[] positions, int vertexCount) {
        int vao = createVAO();
        bindVao(vao);
        writeInVao(0, 3, positions);
        unbindVao();
        return new Entity(vao, vertexCount);
    }

}

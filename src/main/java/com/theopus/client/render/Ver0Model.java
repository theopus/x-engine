package com.theopus.client.render;

public class Ver0Model {
    private float[] vertexes;
    private int[] indexes;

    public Ver0Model(float[] vertexes, int[] indexes) {
        this.vertexes = vertexes;
        this.indexes = indexes;
    }

    public float[] getVertexes() {
        return vertexes;
    }

    public int[] getIndexes() {
        return indexes;
    }
}

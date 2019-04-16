package com.theopus.xengine.client;

public class TerrainData {

    public final float vertexes[];
    public final float uvs[];
    public final int indexes[];

    public TerrainData(float[] vertexes, float[] uvs, int[] indexes) {
        this.vertexes = vertexes;
        this.uvs = uvs;
        this.indexes = indexes;
    }
}

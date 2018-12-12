package com.theopus.xengine.core.render.modules.v1;

public class Ver1Data {

    public final float[] position;
    public final float[] uv;
    public final int[] indexes;
    public final String texturePath;

    public Ver1Data(float[] positions, float[] uvs, int[] indexes, String texturePath) {
        this.position = positions;
        this.uv = uvs;
        this.indexes = indexes;
        this.texturePath = texturePath;
    }
}

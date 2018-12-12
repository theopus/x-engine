package com.theopus.xengine.core.render.modules.v0;

public class Ver0Data {
    private float[] positions;
    private int[] indexes;

    public Ver0Data(float[] positions, int[] indexes) {
        this.positions = positions;
        this.indexes = indexes;
    }

    public float[] getPositions() {
        return positions;
    }

    public void setPositions(float[] positions) {
        this.positions = positions;
    }

    public int[] getIndexes() {
        return indexes;
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }
}

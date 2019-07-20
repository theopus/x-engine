package com.theopus.xengine.core.render.abstraction;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL15;

public class BufferLayout {

    public enum DataType {
        FLOAT(4 * 1, 1, GL15.GL_FLOAT),
        INT(4 * 1, 1, GL15.GL_INT),
        UINT(4 * 1, 1, GL15.GL_UNSIGNED_INT),
        FLOAT2(4 * 2, 2, GL15.GL_FLOAT),
        FLOAT3(4 * 3, 3, GL15.GL_FLOAT),
        FLOAT4(4 * 4, 4, GL15.GL_FLOAT),
        ;

        private final int bytes;
        private final int size;
        private final int glType;

        DataType(int bytes, int size, int glType) {

            this.bytes = bytes;
            this.size = size;
            this.glType = glType;
        }
    }

    public class BufferLayoutEntry {
        private int index;
        private String title;
        private final int size;
        private DataType dataType;

        public BufferLayoutEntry(int size, String title, DataType dataType) {
            this.size = size;
            this.title = title;
            this.dataType = dataType;
        }

        public int getIndex() {
            return index;
        }

        public String getTitle() {
            return title;
        }

        public DataType getDataType() {
            return dataType;
        }
    }

    public List<BufferLayoutEntry> layouts = new ArrayList<>();

    public static BufferLayout of(DataType dataType, String title) {
        return new BufferLayout().add(dataType, title);
    }

    private BufferLayout add(DataType dataType, String title) {
        layouts.add(new BufferLayoutEntry(layouts.size(), title, dataType));
        return this;
    }
}

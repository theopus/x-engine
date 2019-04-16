package com.theopus.xengine.wrapper.opengl.objects;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import com.theopus.xengine.wrapper.opengl.utils.GlToString;

public class Attribute {

    /**
     * stride - data pack size
     * <p>
     * stride = 3 float + 2 float = 3 * 4 + 2 * 4 = 20 bytes
     * <p>
     * attr0 offset(pointer) = 0
     * attr1 offset(pointer) = 3 float = 3 * 4 = 12 bytes
     * <p>
     * offset + stride * dataIndex -- gives open gl position of data
     * <p>
     * b 0           12        20          32        40          52        60
     * |   attr0   |  attr1  |   attr0   |  attr1  |   attr0   |  attr1  |
     * |-----------|---------|-----------|---------|-----------|---------|
     * |px0|py0|pz0|uvx0|uvy0|px1|py1|pz1|uvx1|uvy1|px2|py2|pz2|uvx2|uvy2|
     * |-----------|---------------------|---------|---------------------|
     * |---------->|         |           |
     * |           | <- offset(pointer)  |
     * |           |         |           |
     * |           |-------------------->|
     * |                     |           | <- stride
     * |
     * |-------------------->|
     * | <- stride too
     * <p>
     * if stride = 0 -- means that data fully packed could be set 0 if vbo contains data only for one instance;
     * basically 0 means = size * datatype
     * non 0 values used when same vbo in for few VA pointers
     * used for open gl as step size in buffer to get next portion of data
     * ex. 12 byte current data (uv0) to reach uv2 we need simply add stride: 12 + 20 = 32 bytes - offset for uv1
     * <p>
     * Better always explicitly specify stride, even for single attr vbos
     * <p>
     * if poiter = 0 -- means that vbo buffer starts with beginning;
     * for first attr should be 0 too
     * non 0 value role as offset in vbo for that value
     */
    final int index;
    final int size;
    final int type;
    final int stride;
    final int pointer; //offset
    final Vbo vbo;
    final boolean instanced;

    private Attribute(int index, int size, int type, int stride, int pointer, Vbo vbo, boolean instanced) {
        this.index = index;
        this.size = size;
        this.type = type;
        this.stride = stride;
        this.pointer = pointer;
        this.vbo = vbo;
        this.instanced = instanced;
    }

    public static Attribute singleVboAttribute(int index, GlDataType type, Vbo vbo) {
        return new Attribute(index, type.size, type.glType, type.byteSize, 0, vbo, false);
    }

    private static Attribute sharedVboAttribute(int index, int type, Vbo vbo, int size, int stride, int pointer) {
        return new Attribute(index, size, type, stride, pointer, vbo, false);
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "index=" + index +
                ", size=" + size +
                ", type=" + GlToString.dataType(type) +
                ", stride=" + stride +
                ", pointer=" + pointer +
                ", vbo=" + vbo +
                ", instanced=" + instanced +
                '}';
    }

    public static class SharedAttributeBuilder {

        private final Vbo sharedVbo;
        private int currentPointer = 0;
        private int indexPointer;
        private int stride;
        private List<Entry> attributeEntry;

        public SharedAttributeBuilder(Vbo sharedVbo) {
            this.sharedVbo = sharedVbo;
            attributeEntry = new ArrayList<>();
        }

        public SharedAttributeBuilder add(GlDataType type) {
            int packBytesSize = type.byteSize;
            this.stride += packBytesSize;
            if (this.stride > sharedVbo.size) {
                throw new RuntimeException("To big for selected vbo");
            }

            attributeEntry.add(new Entry(indexPointer, type, type.size, currentPointer));
            currentPointer += packBytesSize;

            if (packBytesSize <= GlDataType.VEC4_FLOAT.byteSize) {
                indexPointer++;
            } else {
                int div = packBytesSize / GlDataType.VEC4_FLOAT.bytes;
                int mod = packBytesSize % GlDataType.VEC4_FLOAT.bytes;
                if (mod != 0) {
                    indexPointer += div + 1;
                } else {
                    indexPointer += div;
                }
            }
            return this;
        }

        public List<Attribute> build() {
            return attributeEntry.stream()
                    .map(entry -> sharedVboAttribute(entry.index, entry.type.glType, sharedVbo, entry.size, stride, entry.pointer))
                    .collect(Collectors.toList());
        }

        private class Entry {
            int index;
            GlDataType type;
            int size;
            int pointer;

            Entry(int index, GlDataType type, int size, int pointer) {
                this.index = index;
                this.type = type;
                this.size = size;
                this.pointer = pointer;
            }
        }
    }
}

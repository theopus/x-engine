package com.theopus.xengine.wrapper.opengl.objects;

import com.theopus.xengine.wrapper.opengl.utils.GlToString;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GlBuffer {
    protected final int id;
    protected final int size;
    protected final int type;
    protected final int usage;

    public GlBuffer(int size, int type, int usage) {
        this.type = type;
        this.usage = usage;
        this.id = genBuffer();
        this.size = bufferSize(size);
    }

    public GlBuffer(int[] data, int type, int usage) {
        this.type = type;
        this.usage = usage;
        this.id = genBuffer();
        this.size = data.length * 4;
        bufferData(data);
    }

    public GlBuffer(float[] data, int type, int usage) {
        this.type = type;
        this.usage = usage;
        this.id = genBuffer();
        this.size = data.length * 4;
        bufferData(data);
    }

    public void bind() {
        GL15.glBindBuffer(type, id);
    }

    public void unbind() {
        GL15.glBindBuffer(type, 0);
    }


    protected static int genBuffer() {
        return GL15.glGenBuffers();
    }


    public int bufferSize(int size) {
        bind();
        GL15.glBufferData(type, size, usage);
        unbind();
        return size;
    }

    public void bufferData(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data);
        buffer.flip();
        bufferData(buffer);
        buffer.clear();
    }

    public void bufferData(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data);
        buffer.flip();
        bufferData(buffer);
        buffer.clear();
    }

    public void bufferData(FloatBuffer buffer) {
        bind();
        GL15.glBufferData(type, buffer, usage);
        unbind();
    }

    public void bufferData(IntBuffer buffer) {
        bind();
        GL15.glBufferData(type, buffer, usage);
        unbind();
    }

    public void bufferSubData(int offset, FloatBuffer buffer) {
        bind();
        GL30.glBufferSubData(type, offset, buffer);
        unbind();
    }

    public int getId() {
        return id;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "GlBuffer{" +
                "id=" + id +
                ", size=" + size +
                ", type=" + GlToString.bufferType(type) +
                ", usage=" + GlToString.bufferUsage(usage) +
                '}';
    }
}

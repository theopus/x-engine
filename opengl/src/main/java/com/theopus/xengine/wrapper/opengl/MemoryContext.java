package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.opengl.objects.GlBuffer;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MemoryContext implements Closeable {

    private Map<Vao, String> vaos;
    private Map<GlBuffer, String> buffers;
    private Map<Texture, String> textures;

    public MemoryContext() {
        this.vaos = new HashMap<>();
        this.buffers = new HashMap<>();
        this.textures = new HashMap<>();
    }

    public void put(Vao vao) {
        vaos.put(vao, "Undefined");
    }

    public void put(GlBuffer glBuffer) {
        buffers.put(glBuffer, "Undefined");
    }

    public void put(Texture texture) {
        textures.put(texture, "Undefined");
    }

    @Override
    public void close() {
        vaos.keySet().stream().map(Vao::getId).forEach(GL30::glDeleteVertexArrays);
        buffers.keySet().stream().map(GlBuffer::getId).forEach(GL15::glDeleteBuffers);
        textures.keySet().stream().map(Texture::getId).forEach(GL15::glDeleteTextures);
    }
}

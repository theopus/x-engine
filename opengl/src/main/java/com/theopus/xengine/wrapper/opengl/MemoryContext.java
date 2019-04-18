package com.theopus.xengine.wrapper.opengl;

import java.io.Closeable;
import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;

import com.theopus.xengine.wrapper.opengl.objects.Cubemap;
import com.theopus.xengine.wrapper.opengl.objects.GlBuffer;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.objects.Vao;

public class MemoryContext implements Closeable {

    private Map<Vao, String> vaos;
    private Map<GlBuffer, String> buffers;
    private Map<Texture, String> textures;
    private Map<Cubemap, String> cubemaps;

    public MemoryContext() {
        this.vaos = new HashMap<>();
        this.buffers = new HashMap<>();
        this.textures = new HashMap<>();
        this.cubemaps = new HashMap<>();
    }

    public void put(Vao vao) {
        vaos.put(vao, "Undefined");
    }

    public void put(GlBuffer... glBuffer) {
        for (GlBuffer buffer : glBuffer) {
            buffers.put(buffer, "Undefined");
        }
    }

    public void put(Texture texture) {
        textures.put(texture, "Undefined");
    }

    @Override
    public void close() {
        vaos.keySet().stream().map(Vao::getId).forEach(GL30::glDeleteVertexArrays);
        buffers.keySet().stream().map(GlBuffer::getId).forEach(GL15::glDeleteBuffers);
        textures.keySet().stream().map(Texture::getId).forEach(GL15::glDeleteTextures);
        cubemaps.keySet().stream().map(Cubemap::getId).forEach(GL15::glDeleteTextures);
    }

    public void put(Cubemap texture) {
        cubemaps.put(texture, "Undefined");
    }
}

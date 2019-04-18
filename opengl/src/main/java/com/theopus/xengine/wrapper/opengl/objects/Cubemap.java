package com.theopus.xengine.wrapper.opengl.objects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL21;
import org.lwjgl.system.MemoryUtil;

import com.theopus.xengine.wrapper.opengl.Loader;
import com.theopus.xengine.wrapper.opengl.MemoryContext;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Cubemap {

    public static final float skyboxVertices[] = {
            // positions
            -1.0f,  1.0f, -1.0f,
            -1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f, -1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,

            -1.0f, -1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f, -1.0f,  1.0f,
            -1.0f, -1.0f,  1.0f,

            -1.0f,  1.0f, -1.0f,
            1.0f,  1.0f, -1.0f,
            1.0f,  1.0f,  1.0f,
            1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f,  1.0f,
            -1.0f,  1.0f, -1.0f,

            -1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f, -1.0f,
            1.0f, -1.0f, -1.0f,
            -1.0f, -1.0f,  1.0f,
            1.0f, -1.0f,  1.0f
    };

    public final int textureId;

    public Cubemap(int textureId) {
        this.textureId = textureId;
    }

    public static Cubemap loadTexture(List<String> paths, MemoryContext context) {
        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL21.GL_TEXTURE_CUBE_MAP, textureId);

        for (int i = 0; i < paths.size(); i++) {
            String path = paths.get(i);
            loadImage(path, i);
        }

        Cubemap texture = new Cubemap(textureId);
        GL11.glTexParameteri(GL21.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL21.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL21.GL_TEXTURE_CUBE_MAP, GL21.GL_TEXTURE_WRAP_S, GL21.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL21.GL_TEXTURE_CUBE_MAP, GL21.GL_TEXTURE_WRAP_T, GL21.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL21.GL_TEXTURE_CUBE_MAP, GL21.GL_TEXTURE_WRAP_R, GL21.GL_CLAMP_TO_EDGE);
        GL11.glBindTexture(GL21.GL_TEXTURE_CUBE_MAP, 0);
        context.put(texture);
        return texture;
    }

    private static void loadImage(String path, int n){
        try (InputStream resourceAsStream = Loader.class.getClassLoader().getResourceAsStream(path);) {
            PNGDecoder decoder = new PNGDecoder(resourceAsStream);
            int width = decoder.getWidth();
            int height = decoder.getHeight();

            ByteBuffer byteBuffer = MemoryUtil.memAlloc(4 * width * height);
            decoder.decode(byteBuffer, height * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();

            System.out.println(path);
            System.out.println(n);
            GL11.glTexImage2D(GL21.GL_TEXTURE_CUBE_MAP_POSITIVE_X + n, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            byteBuffer.clear();
            MemoryUtil.memFree(byteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void bind(){
        GL21.glBindTexture(GL21.GL_TEXTURE_CUBE_MAP, textureId);
    }

    public void unbind(){
        GL21.glBindTexture(GL21.GL_TEXTURE_CUBE_MAP, 0);
    }

    public int getId() {
        return textureId;
    }
}

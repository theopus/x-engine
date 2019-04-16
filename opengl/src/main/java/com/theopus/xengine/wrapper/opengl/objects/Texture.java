package com.theopus.xengine.wrapper.opengl.objects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.theopus.xengine.wrapper.opengl.Loader;

import de.matthiasmann.twl.utils.PNGDecoder;

public class Texture {
    private final int id;
    private final int width;
    private final int height;
    private boolean mipmap;

    public Texture(int id, int width, int height) {
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public static Texture loadTexture(String path) {
        try (InputStream resourceAsStream = Loader.class.getClassLoader().getResourceAsStream(path);) {
            PNGDecoder decoder = new PNGDecoder(resourceAsStream);
            int width = decoder.getWidth();
            int height = decoder.getHeight();

            int textureId = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

            ByteBuffer byteBuffer = MemoryUtil.memAlloc(4 * width * height);
            decoder.decode(byteBuffer, height * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            byteBuffer.clear();
            MemoryUtil.memFree(byteBuffer);


            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            return new Texture(textureId, width, height);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateMipmap() {
        bind();
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);
        unbind();
        mipmap = true;
    }

    public void bind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
    }

    public void unbind() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public int getId() {
        return id;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMipmap() {
        return mipmap;
    }

    @Override
    public String toString() {
        return "Texture{" +
                "id=" + id +
                ", width=" + width +
                ", height=" + height +
                ", mipmap=" + mipmap +
                '}';
    }
}

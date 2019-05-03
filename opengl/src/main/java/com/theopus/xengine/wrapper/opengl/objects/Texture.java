package com.theopus.xengine.wrapper.opengl.objects;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

import com.theopus.xengine.wrapper.opengl.Loader;
import com.theopus.xengine.wrapper.opengl.MemoryContext;

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

    public static Texture loadTexture(String path, MemoryContext context) {
        try (InputStream resourceAsStream = Loader.class.getClassLoader().getResourceAsStream(path);) {
            PNGDecoder decoder = new PNGDecoder(resourceAsStream);
            int width = decoder.getWidth();
            int height = decoder.getHeight();

            int textureId = GL11.glGenTextures();

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

            ByteBuffer byteBuffer = MemoryUtil.memAlloc(4 * width * height);
            decoder.decode(byteBuffer, height * 4, PNGDecoder.Format.RGBA);
            byteBuffer.flip();
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, byteBuffer);
            byteBuffer.clear();
            MemoryUtil.memFree(byteBuffer);


            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            Texture texture = new Texture(textureId, width, height);
            context.put(texture);
            return texture;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Texture emptyTexture(int width, int height, MemoryContext context){

        int textureId = GL11.glGenTextures();

        bind(textureId);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (int[]) null);

        unbind(textureId);
        Texture texture = new Texture(textureId, width, height);
        context.put(texture);
        return texture;
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
        bind(id);
    }

    public void unbind() {
        unbind(id);
    }

    public static void unbind(int id) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }

    public static void bind(int id){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
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

    public void close() {
        GL15.glDeleteTextures(id);
    }
}

package com.theopus.xengine.wrapper.opengl.objects;

import org.lwjgl.opengl.GL30;

import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.utils.GL2String;

public class Rbo {

    private final int id;
    private final int type;

    public Rbo(int width, int height, int type) {
        this.id = generate();
        bind();
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL30.GL_DEPTH24_STENCIL8, width, height);
        unbind();
        this.type = type;
    }

    public static int generate() {
        return GL30.glGenRenderbuffers();
    }

    public void bind() {
        bind(id);
    }

    public void unbind() {
        unbind(id);
    }

    public static void bind(int id) {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
    }

    public static void unbind(int id) {
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, 0);
    }

    public void close(){
        GL30.glDeleteRenderbuffers(id);
    }

    @Override
    public String toString() {
        return "Rbo{" +
                "id=" + id +
                ", type=" + GL2String.rboType(type) +
                '}';
    }

    public int getId() {
        return id;
    }
}

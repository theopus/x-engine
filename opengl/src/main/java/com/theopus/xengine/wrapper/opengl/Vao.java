package com.theopus.xengine.wrapper.opengl;

public class Vao {

    private final int id;
    private final Vbo ebo;
    private final Vbo[] vbos;
    private final Attribute[] attributes;
    private int length;

    public Vao(int id, Vbo ebo, Vbo[] vbos, Attribute[] attributes, int length) {
        this.id = id;
        this.ebo = ebo;
        this.vbos = vbos;
        this.attributes = attributes;
        this.length = length;
    }

    public int getId() {
        return id;
    }

    public Vbo getEbo() {
        return ebo;
    }

    public Vbo[] getVbos() {
        return vbos;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public int getLength() {
        return length;
    }
}

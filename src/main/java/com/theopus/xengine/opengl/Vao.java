package com.theopus.xengine.opengl;

public class Vao {

    private final int id;
    private final Vbo ebo;
    private final Vbo[] vbos;
    private final Attribute[] attributes;

    public Vao(int id, Vbo ebo, Vbo[] vbos, Attribute[] attributes) {
        this.id = id;
        this.ebo = ebo;
        this.vbos = vbos;
        this.attributes = attributes;
    }
}

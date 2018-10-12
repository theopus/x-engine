package com.theopus.xengine.opengl;

public class Vao {

    private final int id;
    private final Vbo ebo;
    private final Vbo[] vbos;
    private final Attribute[] attributes;

    public Vao() {
        this.id = 0;
        this.ebo = null;
        this.vbos = null;
        this.attributes = null;
    }
}

package com.theopus.xengine.opengl;

public class VaoBuilder {
    private int id;
    private Vbo ebo;
    private Vbo[] vbos;
    private Attribute[] attributes;
    private int length;

    public VaoBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public VaoBuilder setEbo(Vbo ebo) {
        this.ebo = ebo;
        return this;
    }

    public VaoBuilder setVbos(Vbo... vbos) {
        this.vbos = vbos;
        return this;
    }

    public VaoBuilder setAttributes(Attribute... attributes) {
        this.attributes = attributes;
        return this;
    }

    public Vao createVao() {
        return new Vao(id, ebo, vbos, attributes, length);
    }

    public VaoBuilder setVertexes(int length) {
        this.length = length;
        return this;
    }
}
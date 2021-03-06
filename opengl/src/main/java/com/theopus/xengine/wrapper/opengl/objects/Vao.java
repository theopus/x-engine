package com.theopus.xengine.wrapper.opengl.objects;

import java.util.Arrays;
import java.util.Objects;

import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vao {

    private static final Logger LOGGER = LoggerFactory.getLogger(Vao.class);

    private int id;
    private Ebo ebo;
    private Attribute[] attributes;
    private int length;

    public Vao(Ebo ebo, Attribute... attributes) {
        this.id = genVertexArrayBuffer();
        this.ebo = ebo;
        this.length = ebo.lengths;
        this.attributes = attributes;
        initVao();
    }

    public Vao(int length, Attribute... attributes) {
        this.id = genVertexArrayBuffer();
        this.length = length;
        this.attributes = attributes;
        initVao();
    }

    private void initVao() {
        this.bind();
        if (Objects.nonNull(ebo))
            ebo.bind();
        for (Attribute attribute : attributes) {
            initAttr(attribute);
        }
        this.unbind();
        if (Objects.nonNull(ebo))
            ebo.unbind();
    }

    private void initAttr(Attribute attribute) {
        LOGGER.info("Initializing attribute [{}]", attribute);
        attribute.vbo.bind();
        GL30.glVertexAttribPointer(attribute.index, attribute.size, attribute.type, false, attribute.stride, attribute.pointer);
        if (attribute.instanced) {
            GL33.glVertexAttribDivisor(attribute.index, 1);
        }
        GL30.glEnableVertexAttribArray(attribute.index);
        attribute.vbo.unbind();
    }

    private int genVertexArrayBuffer() {
        return GL30.glGenVertexArrays();
    }

    public void bind() {
        GL30.glBindVertexArray(id);
    }

    public static void unbind() {
        GL30.glBindVertexArray(0);
    }

    @Override
    public String toString() {
        return "Vao{" +
                "id=" + id +
                ", ebo=" + ebo +
                ", attributes=" + Arrays.toString(attributes) +
                '}';
    }

    public int getId() {
        return id;
    }

    public Ebo getEbo() {
        return ebo;
    }

    public Attribute[] getAttributes() {
        return attributes;
    }

    public int getLength() {
        return length;
    }
}

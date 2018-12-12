package com.theopus.xengine.wrapper.opengl.objects;

public class TexturedVao {

    public final Vao vao;
    public final Texture texture;

    public TexturedVao(Vao vao, Texture texture) {
        this.vao = vao;
        this.texture = texture;
    }
}

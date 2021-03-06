package com.theopus.xengine.wrapper.opengl.commands;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class SimpleVaoRenderCommand {

    public final StaticShader shader;

    public SimpleVaoRenderCommand(StaticShader shader) {
        this.shader = shader;
    }

    public void prepare(Vao vao) {
        vao.bind();
        shader.bind();
    }

    public void render(Vao vao, Matrix4f transformation) {
        shader.transformation().load(transformation);
        GL30.glDrawElements(GL11.GL_TRIANGLES, vao.getLength(), GL30.GL_UNSIGNED_INT, 0);
    }

    public void finish() {
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    public void close() {
        shader.cleanup();
    }

}

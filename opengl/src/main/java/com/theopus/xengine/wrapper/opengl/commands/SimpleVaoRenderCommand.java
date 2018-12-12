package com.theopus.xengine.wrapper.opengl.commands;

import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class SimpleVaoRenderCommand {

    public final StaticShader shader;

    public SimpleVaoRenderCommand(StaticShader shader) {
        this.shader = shader;
    }

    public void prepare(Vao vao) {
        GL30.glBindVertexArray(vao.getId());
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

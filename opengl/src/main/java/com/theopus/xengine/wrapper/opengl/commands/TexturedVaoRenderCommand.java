package com.theopus.xengine.wrapper.opengl.commands;

import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

public class TexturedVaoRenderCommand {


    public final StaticShader shader;

    public TexturedVaoRenderCommand(StaticShader shader) {
        this.shader = shader;
    }

    public void prepare(TexturedVao obj) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, obj.texture.getId());
        GL30.glBindVertexArray(obj.vao.getId());
        shader.bind();
    }

    public void render(TexturedVao obj, Matrix4f transformation) {
        shader.transformation().load(transformation);
        GL30.glDrawElements(GL11.GL_TRIANGLES, obj.vao.getLength(), GL30.GL_UNSIGNED_INT, 0);
    }

    public void finish() {
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        shader.unbind();
    }

    public void close() {
        shader.cleanup();
    }

}

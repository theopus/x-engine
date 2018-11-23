package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class DefaultRenderCommand {

    public final StaticShader shader;

    public DefaultRenderCommand(StaticShader shader) {
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

    public void loadView(Matrix4f view) {
        shader.view().load(view);
    }

    public void loadProjection(Matrix4f projection) {
        shader.projection().load(projection);
    }
}

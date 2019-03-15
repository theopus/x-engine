package com.theopus.xengine.wrapper.opengl.commands;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class TextRenderCommand {

    public final StaticShader shader;
    private final GlState state;

    public TextRenderCommand(StaticShader shader, GlState state) {
        this.shader = shader;
        this.state = state;
    }

    public void prepare(TexturedVao obj) {
        state.depthTest.update(false);
        state.backFaceCulling.update(false);

        //
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, obj.texture.getId());
        GL30.glBindVertexArray(obj.vao.getId());
        shader.bind();
    }

    public void render(TexturedVao obj) {
        GL30.glDrawArrays(GL11.GL_TRIANGLES, 0, obj.vao.getLength());
    }

    public void finish() {
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        shader.unbind();

        //
        GL11.glDisable(GL11.GL_BLEND);
        //
    }

    public void close() {
        shader.cleanup();
    }
}

package com.theopus.xengine.wrapper.opengl.commands;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class FontRenderCommand {

    public final StaticShader shader;
    private final GlState state;

    public FontRenderCommand(StaticShader shader, GlState state) {
        this.shader = shader;
        this.state = state;
    }

    public void prepare(Texture texture) {
        state.depthTest.update(false);
        state.backFaceCulling.update(false);

        //
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        GL30.glBindVertexArray(texture.getId());
        shader.bind();
    }

    public void render(int vao, int lentgh) {
        GL30.glBindVertexArray(vao);
        GL30.glDrawArrays(GL11.GL_TRIANGLES, 0, lentgh);
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
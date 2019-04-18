package com.theopus.xengine.core.render.gl;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.objects.Texture;

public class FontRenderCommand {

    public final FontShader shader;
    private final GlState state;

    public FontRenderCommand(FontShader shader, GlState state) {
        this.shader = shader;
        this.state = state;
    }

    public void prepareCommand() {
        state.depthTest.update(false);
        state.backFaceCulling.update(false);

        //
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        //
    }

    public void finishCommand() {
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void prepare(Texture texture) {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
        shader.bind();
    }

    public void render(int vao, int lentgh, Vector2f location, Vector3f rgb) {
        shader.transformation().load(location);
        shader.color().load(rgb);
        GL30.glBindVertexArray(vao);
        GL30.glDrawArrays(GL11.GL_TRIANGLES, 0, lentgh);
    }

    public void finish() {
        GL30.glBindVertexArray(0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        shader.unbind();
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void close() {
        shader.cleanup();
    }
}
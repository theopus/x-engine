package com.theopus.xengine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Render {

    private StaticShader staticShader;

    public Render(StaticShader staticShader) {
        this.staticShader = staticShader;
    }

    public void render(Entity entity) {
        GL30.glBindVertexArray(entity.getVaoId());
        GL20.glEnableVertexAttribArray(0);

        staticShader.bind();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, entity.getVertexCount());
        staticShader.unbind();

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}

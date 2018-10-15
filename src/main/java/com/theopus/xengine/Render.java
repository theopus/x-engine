package com.theopus.xengine;

import com.theopus.xengine.trait.custom.RenderTrait;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Render {

    private StaticShader staticShader;

    public Render(StaticShader staticShader) {
        this.staticShader = staticShader;
    }

    public void render(RenderTrait trait) {
        GL30.glBindVertexArray(trait.getVaoId());
        GL20.glEnableVertexAttribArray(0);

        staticShader.bind();
        staticShader.loadTransformationMatrix(
                trait.getTransformation()
        );
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, trait.getVertexCount());
        staticShader.unbind();

        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public void cleanup() {
        staticShader.cleanup();
    }
}

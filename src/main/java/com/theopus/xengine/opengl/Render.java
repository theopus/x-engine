package com.theopus.xengine.opengl;

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
        staticShader.bind();
        GL30.glBindVertexArray(trait.getVaoId());

        staticShader.loadTransformationMatrix(
                trait.getTransformation()
        );
        GL30.glDrawElements(GL11.GL_TRIANGLES, trait.getVertexCount(), GL30.GL_UNSIGNED_INT, 0);

        GL30.glBindVertexArray(0);
        staticShader.unbind();
    }

    public void cleanup() {
        staticShader.cleanup();
    }
}

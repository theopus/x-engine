package com.theopus.xengine.core.render;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class GlRenderer extends BaseRenderer {
    @Override
    public void clearBuffer() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void loadProjectionMatrix(Matrix4f projection) {

    }

    @Override
    public void loadViewMatrix(Matrix4f view) {
        modules.forEach(renderModule -> renderModule.loadViewMatrix(view));
    }
}

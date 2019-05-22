package com.theopus.xengine.core.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import com.artemis.annotations.Wire;
import com.theopus.xengine.core.render.modules.m2d.GlModule2D;
import com.theopus.xengine.core.render.modules.m2d.RenderModule2D;
import com.theopus.xengine.wrapper.opengl.objects.Fbo;

public class GlRenderer extends BaseRenderer {
    private final GLContext glContext;

    public GlRenderer(GLContext glContext) {
        this.glContext = glContext;
    }

    @Override
    public void clearBuffer() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void init() {
        GlModule2D renderModule2D = (GlModule2D) get(RenderModule2D.class);
        renderModule2D.add(BaseRenderer.MAIN_SCREEN_TEXTURE, glContext.getFbo().getColorAttachment());
    }

    public GLContext getGlContext() {
        return glContext;
    }

    @Override
    public void loadProjectionMatrix(Matrix4f projection) {
        glContext.getMatricesBlock().loadProjectionMatrix(projection);
    }

    @Override
    public void render() {
//        Fbo fbo = glContext.getFbo();
//        fbo.bind();
//        clearBuffer();
//        super.render();
//        fbo.unbind();
        super.render();
    }

    @Override
    public void loadViewMatrix(Matrix4f view) {
        glContext.getMatricesBlock().loadViewMatrix(view);
    }

    @Override
    public void loadFramebufferSize(int width, int height) {
        GL11.glViewport(0, 0, width, height);
        glContext.getFbo().update(width, height);
    }

    @Override
    public void loadLight(Vector3f diffuse, Vector3f position) {
        glContext.getLightBlock().loadInensity(diffuse);
        glContext.getLightBlock().loadPosition(position);
    }
}

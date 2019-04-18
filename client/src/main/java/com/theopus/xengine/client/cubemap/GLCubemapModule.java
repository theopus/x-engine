package com.theopus.xengine.client.cubemap;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL21;

import com.artemis.ArchetypeBuilder;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.Render;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.objects.Attribute;
import com.theopus.xengine.wrapper.opengl.objects.Cubemap;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.objects.Vbo;
import com.theopus.xengine.wrapper.opengl.shader.ShaderProgram;
import com.theopus.xengine.wrapper.opengl.shader.SimpleShader;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;

public class GLCubemapModule extends CubemapModule<Cubemap> {

    @Wire
    private GLContext context;

    private Vao vao;
    private ShaderProgram shader;
    private GlState state;

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(Render.class);
    }

    @Override
    public void init() {
        vao = new Vao(36,
                Attribute.singleVboAttribute(0, GlDataType.VEC3_FLOAT,
                        new Vbo(Cubemap.skyboxVertices, GL15.GL_STATIC_DRAW)));

        shader = new SimpleShader("cubemap/shader.vert", "cubemap/shader.frag");
        state = context.getState();
        shader.bindUniformBlock(context.getMatricesBlock());
    }

    @Override
    public void render(int entityId, Cubemap cubemap) {
        GL15.glDrawArrays(GL11.GL_TRIANGLES, 0, 36);
    }

    @Override
    public Cubemap load(CubemapData d) {
        return Cubemap.loadTexture(Arrays.asList(
                d.right,
                d.left,
                d.top,
                d.bottom,
                d.back,
                d.front
        ), context.getMemoryContext());
    }

    @Override
    public void prepare(Cubemap cubemap) {
        cubemap.bind();
    }

    @Override
    public void finish(Cubemap cubemap) {
        cubemap.unbind();
    }

    @Override
    public void prepareModule() {
        state.depthFunciton.update(GL11.GL_LEQUAL);
        state.depthMask.update(false);
        vao.bind();
        shader.bind();
    }

    @Override
    public void finishModule() {
        state.depthFunciton.update(GL11.GL_LESS);
        state.depthMask.update(true);
        vao.unbind();
        shader.unbind();
    }
}

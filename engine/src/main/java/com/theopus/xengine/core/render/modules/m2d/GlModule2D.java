package com.theopus.xengine.core.render.modules.m2d;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.artemis.ArchetypeBuilder;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.Transformation2D;
import com.theopus.xengine.core.ecs.components.TransformationMatrix2D;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.objects.Attribute;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.objects.Vbo;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;

public class GlModule2D extends RenderModule2D<Texture> {

    @Wire
    private GLContext context;
    private StaticShader shader;
    private Vao quad;
    private MemoryContext memoryContext;
    private GlState state;

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(Transformation2D.class, TransformationMatrix2D.class);
    }

    @Override
    public void init() {
        shader = new StaticShader("m2d/shader.vert", "m2d/shader.frag");
        float positions[] = {
                1.0f, -1.0f,
                1.0f,  1.0f,
                -1.0f, -1.0f,
                -1.0f,  1.0f,
        };

        float uv[] = {
                1.0f, 1.0f,
                1.0f, 0.0f,
                0.0f, 1.0f,
                 0.0f, 0.0f,
        };
        quad = new Vao(positions.length/2,
                Attribute.singleVboAttribute(0, GlDataType.VEC2_FLOAT, new Vbo(positions, GL15.GL_STATIC_DRAW)),
                Attribute.singleVboAttribute(1, GlDataType.VEC2_FLOAT, new Vbo(uv, GL15.GL_STATIC_DRAW))
                );
        memoryContext = context.getMemoryContext();
        state = context.getState();
    }

    @Override
    public void render(int entityId, Texture texture) {
        Matrix4f model = mTransformationMtx.get(entityId).model;
        shader.transformation().load(model);
        GL15.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
    }

    @Override
    public Texture load(String d) {
        return Texture.loadTexture(d, memoryContext);
    }

    @Override
    public void prepare(Texture texture) {
        texture.bind();
    }

    @Override
    public void finish(Texture texture) {
        texture.unbind();
    }

    @Override
    public void prepareModule() {
        quad.bind();
        shader.bind();
        state.blending.update(true);
        state.depthTest.update(false);
        state.alphaBlend.update(true);
    }

    @Override
    public void finishModule() {
        quad.unbind();
        shader.unbind();
        state.blending.update(false);
        state.depthTest.update(true);
    }

    public void renderTexture(Texture texture){
        prepareModule();
        prepare(texture);
        shader.transformation().load(new Matrix4f());
        GL15.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
        finish(texture);
        finishModule();
    }
}

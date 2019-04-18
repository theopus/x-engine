package com.theopus.xengine.core.render.modules.v0;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.SimpleVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class GLVer0Module extends ArtemisRenderModule<Ver0Data, Vao> {
    private SimpleLoader loader;

    @Wire
    private ComponentMapper<TransformationMatrix> mTransformationMtx;

    @Wire
    private GLContext context;
    private StaticShader shader;

    public void init() {
        shader = new StaticShader("v0/static.vert", "v0/static.frag");
        loader = new SimpleLoader(context.getMemoryContext());
        shader.bindUniformBlock(context.getMatricesBlock());
    }

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(TransformationMatrix.class);
    }

    @Override
    public void render(int entityId, Vao vao) {
        shader.transformation().load(mTransformationMtx.get(entityId).model);
        GL30.glDrawElements(GL11.GL_TRIANGLES, vao.getLength(), GL30.GL_UNSIGNED_INT, 0);
    }

    @Override
    public Vao load(Ver0Data d) {
        return loader.load(d.getPositions(), d.getIndexes());
    }

    @Override
    public void prepare(Vao vao) {
        vao.bind();
    }

    @Override
    public void finish(Vao vao) {
        vao.unbind();
    }

    @Override
    public void prepareModule() {
        shader.bind();
    }

    @Override
    public void finishModule() {
        shader.unbind();
    }
}

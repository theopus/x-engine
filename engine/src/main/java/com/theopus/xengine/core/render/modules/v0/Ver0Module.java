package com.theopus.xengine.core.render.modules.v0;

import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.SimpleVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class Ver0Module extends ArtemisRenderModule<Ver0Data, Vao> {
    private SimpleVaoRenderCommand renderCommand;
    private SimpleLoader loader;

    @Wire
    private ComponentMapper<TransformationMatrix> mMapper;

    @Wire
    private GLContext context;

    public void init(){
        StaticShader staticShader = new StaticShader("v0/static.vert", "v0/static.frag");
        renderCommand = new SimpleVaoRenderCommand(staticShader);
        loader = new SimpleLoader(context.getMemoryContext());
        staticShader.bindUniformBlock(context.getMatricesBlock());
    }

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(TransformationMatrix.class);
    }

    @Override
    public void renderModel(int entityId, Vao vao) {
        renderCommand.render(vao, mMapper.get(entityId).model);
    }

    @Override
    public Vao loadModel(Ver0Data d) {
        return loader.load(d.getPositions(), d.getIndexes());
    }

    @Override
    public void prepareModel(Vao vao) {
        renderCommand.prepare(vao);
    }

    @Override
    public void finishModel(Vao vao) {
        renderCommand.prepare(vao);
    }


}

package com.theopus.xengine.core.render.modules.v3;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class Ver3Module extends ArtemisRenderModule<Ver3Data, TexturedVao> {
    private TexturedVaoRenderCommand renderCommand;
    private SimpleLoader loader;

    @Wire
    private ComponentMapper<ModelMatrix> mMapper;

    @Override
    public void setContext(GLContext glContext) {
        StaticShader staticShader = new StaticShader("v3/static.vert", "v3/static.frag");
        renderCommand = new TexturedVaoRenderCommand(staticShader, glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());

        staticShader.bindUniformBlock(glContext.getMatricesBlock());
        staticShader.bindUniformBlock(glContext.getLightBlock());
        staticShader.bindUniformBlock(glContext.getMaterialBlock());
    }

    @Override
    public void renderModel(int entityId, TexturedVao texturedVao) {
        renderCommand.render(texturedVao, mMapper.get(entityId).model);
    }

    @Override
    public TexturedVao loadModel(Ver3Data d) {
        return loader.load(d.obj);
    }

    @Override
    public void prepareModel(TexturedVao texturedVao) {
        renderCommand.prepare(texturedVao);
    }

    @Override
    public void finishModel(TexturedVao texturedVao) {
        renderCommand.finish();
    }
}

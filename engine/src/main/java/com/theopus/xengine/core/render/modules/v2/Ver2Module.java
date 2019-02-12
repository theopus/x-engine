package com.theopus.xengine.core.render.modules.v2;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class Ver2Module extends ArtemisRenderModule<Ver2Data, TexturedVao> {
    private TexturedVaoRenderCommand renderCommand;
    private SimpleLoader loader;

    @Wire
    private ComponentMapper<ModelMatrix> mMapper;

    @Override
    public void setContext(GLContext glContext) {
        StaticShader staticShader = new StaticShader("v2/static.vert", "v2/static.frag");
        renderCommand = new TexturedVaoRenderCommand(staticShader, glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());
        staticShader.bindUniformBlock(glContext.getMatricesBlock());
        staticShader.bindUniformBlock(glContext.getLightBlock());
    }

    @Override
    public void renderModel(int entityId, TexturedVao texturedVao) {
        renderCommand.render(texturedVao, mMapper.get(entityId).model);
    }

    @Override
    public TexturedVao loadModel(Ver2Data d) {
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

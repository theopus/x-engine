package com.theopus.xengine.core.render.modules.v1;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class Ver1Module extends ArtemisRenderModule<Ver1Data, TexturedVao> {
    private TexturedVaoRenderCommand renderCommand;
    private SimpleLoader loader;

    @Wire
    private ComponentMapper<ModelMatrix> mMapper;

    public Ver1Module(GLContext glContext) {
        StaticShader staticShader = new StaticShader("v1/static.vert", "v1/static.frag");
        renderCommand = new TexturedVaoRenderCommand(staticShader, glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());
        staticShader.bindUniformBlock(glContext.getMatricesBlock());
    }

    @Override
    public void renderModel(int entityId, TexturedVao texturedVao) {
        renderCommand.render(texturedVao, mMapper.get(entityId).model);
    }

    @Override
    public TexturedVao loadModel(Ver1Data d) {
        return loader.load(d.position, d.uv, d.indexes, d.texturePath);
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

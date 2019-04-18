package com.theopus.xengine.client;

import com.artemis.annotations.Wire;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class GlTerrainModule extends TerrainModule<TexturedVao> {

    @Wire
    private GLContext context;
    private TexturedVaoRenderCommand renderCommand;
    private SimpleLoader loader;

    @Override
    public void init() {
        super.init();
        StaticShader shader = new StaticShader("grid/shader.vert", "grid/shader.frag");
        renderCommand = new TexturedVaoRenderCommand(
                shader,
                context.getState());
        shader.bindUniformBlock(context.getMatricesBlock());
        loader = new SimpleLoader(context.getMemoryContext());
    }

    @Override
    public void render(int entityId, TexturedVao texturedVao) {
        renderCommand.render(texturedVao, mTransformationMtx.get(entityId).model);
    }

    @Override
    public TexturedVao load(TerrainData d) {
        return loader.load(d.vertexes, d.uvs, d.indexes, "textures/white.png");
    }

    @Override
    public void prepare(TexturedVao texturedVao) {
        renderCommand.prepare(texturedVao);
    }

    @Override
    public void finish(TexturedVao texturedVao) {
        renderCommand.finish();
    }
}

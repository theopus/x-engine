package com.theopus.xengine.core.render.modules;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.wrapper.opengl.DefaultRenderCommand;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.Vao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class Ver0Module extends ArtemisRenderModule<Ver0Data, Vao> {

    private DefaultRenderCommand renderCommand;
    private SimpleLoader loader = new SimpleLoader();

    @Wire
    private ComponentMapper<ModelMatrix> mMapper;

    public Ver0Module() {
        StaticShader staticShader = new StaticShader("static.vert", "static.frag");
        renderCommand = new DefaultRenderCommand(staticShader);
    }

    @Override
    public void renderModel(int entityId, Vao vao) {
        renderCommand.render(vao, mMapper.get(entityId).model);
    }

    @Override
    public Vao loadModel(Ver0Data d) {
        return loader.loadSimpleVao(d.getPositions(), d.getIndexes());
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

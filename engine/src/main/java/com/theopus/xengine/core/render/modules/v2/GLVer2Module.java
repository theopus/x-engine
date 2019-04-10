package com.theopus.xengine.core.render.modules.v2;

import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class GLVer2Module extends Ver2Module<TexturedVao> {
    private TexturedVaoRenderCommand renderCommand;
    private SimpleLoader loader;

    @Wire
    private ComponentMapper<TransformationMatrix> mMapper;

    @Wire
    private GLContext glContext;

    public void init() {
        StaticShader staticShader = new StaticShader("v2/static.vert", "v2/static.frag");
        renderCommand = new TexturedVaoRenderCommand(staticShader, glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());
        staticShader.bindUniformBlock(glContext.getMatricesBlock());
        staticShader.bindUniformBlock(glContext.getLightBlock());
    }

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(TransformationMatrix.class);
    }

    @Override
    public void render(int entityId, TexturedVao texturedVao) {
        renderCommand.render(texturedVao, mMapper.get(entityId).model);
    }

    @Override
    public TexturedVao load(Ver2Data d) {
        return loader.load(d.obj);
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

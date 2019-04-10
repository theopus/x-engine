package com.theopus.xengine.core.render.modules.v3;

import com.artemis.ArchetypeBuilder;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.Material;
import com.theopus.xengine.wrapper.opengl.objects.MaterialVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import com.theopus.xengine.wrapper.opengl.shader.ubos.MaterialUniformBlock;

public class GLVer3Module extends Ver3Module<MaterialVao> {
    private TexturedVaoRenderCommand renderCommand;
    private SimpleLoader loader;
    private MaterialUniformBlock materialBlock;

    @Wire
    private ComponentMapper<TransformationMatrix> mMapper;

    @Wire
    private GLContext glContext;

    public void init() {
        StaticShader staticShader = new StaticShader("v3/static.vert", "v3/static.frag");
        renderCommand = new TexturedVaoRenderCommand(staticShader, glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());
        materialBlock = glContext.getMaterialBlock();
        staticShader.bindUniformBlock(glContext.getMatricesBlock());
        staticShader.bindUniformBlock(glContext.getLightBlock());
        staticShader.bindUniformBlock(glContext.getMaterialBlock());
    }

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(TransformationMatrix.class);
    }

    @Override
    public void render(int entityId, MaterialVao vao) {
        renderCommand.render(vao.texturedVao, mMapper.get(entityId).model);
        materialBlock.loadMaterial(vao.material);
    }

    @Override
    public MaterialVao load(Ver3Data d) {
        return loader.load(d.obj, new Material(d.ambient, d.diffuse, d.specular, d.shininess));
    }

    @Override
    public void prepare(MaterialVao vao) {
        renderCommand.prepare(vao.texturedVao);
    }

    @Override
    public void finish(MaterialVao vao) {
        renderCommand.finish();
    }
}

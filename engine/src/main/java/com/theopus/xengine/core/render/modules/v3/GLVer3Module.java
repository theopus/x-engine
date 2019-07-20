package com.theopus.xengine.core.render.modules.v3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TexturedVaoRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.Material;
import com.theopus.xengine.wrapper.opengl.objects.MaterialVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import com.theopus.xengine.wrapper.opengl.shader.ubos.MaterialUniformBlock;

public class GLVer3Module extends Ver3Module<MaterialVao> {
    private SimpleLoader loader;
    private MaterialUniformBlock materialBlock;

    @Wire
    private ComponentMapper<TransformationMatrix> mMapper;

    @Wire
    private GLContext glContext;
    private GlState state;
    private StaticShader shader;

    public void init() {
        shader = new StaticShader("v3/static.vert", "v3/static.frag");
        loader = new SimpleLoader(glContext.getMemoryContext());
        materialBlock = glContext.getMaterialBlock();
        shader.bindUniformBlock(glContext.getMatricesBlock());
        shader.bindUniformBlock(glContext.getLightBlock());
        shader.bindUniformBlock(glContext.getMaterialBlock());
        state = glContext.getState();
    }

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(TransformationMatrix.class);
    }

    @Override
    public void render(int entityId, MaterialVao vao) {
        materialBlock.loadMaterial(vao.material);
        shader.transformation().load(mMapper.get(entityId).model);
        GL30.glDrawElements(GL11.GL_TRIANGLES, vao.texturedVao.vao.getLength(), GL30.GL_UNSIGNED_INT, 0);
    }

    @Override
    public MaterialVao load(Ver3Data d) {
        return loader.load(d.obj, new Material(d.ambient, d.diffuse, d.specular, d.shininess));
    }

    @Override
    public void prepare(MaterialVao vao) {
        vao.texturedVao.vao.bind();
        vao.texturedVao.texture.bind();
    }

    @Override
    public void finish(MaterialVao vao) {
        vao.texturedVao.vao.unbind();
        vao.texturedVao.texture.unbind();
    }

    @Override
    public void prepareModule() {
        shader.bind();
        state.depthTest.update(true);
        state.backFaceCulling.update(true);
    }

    @Override
    public void finishModule() {
        shader.unbind();
    }
}

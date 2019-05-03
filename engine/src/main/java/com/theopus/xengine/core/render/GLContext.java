package com.theopus.xengine.core.render;

import java.io.Closeable;

import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.objects.Fbo;
import com.theopus.xengine.wrapper.opengl.shader.ubos.LightUniformBlock;
import com.theopus.xengine.wrapper.opengl.shader.ubos.MaterialUniformBlock;
import com.theopus.xengine.wrapper.opengl.shader.ubos.MatricesUniformBlock;

public class GLContext implements Closeable {
    private LightUniformBlock lightBlock;
    private MatricesUniformBlock matricesBlock;
    private MaterialUniformBlock materialBlock;
    private MemoryContext memoryContext;
    private GlState state;
    private Fbo fbo;

    public void init() {
        this.memoryContext = new MemoryContext();
        this.matricesBlock = MatricesUniformBlock.withCtx(0, memoryContext);
        this.lightBlock = LightUniformBlock.withCtx(1, memoryContext);
        this.materialBlock = MaterialUniformBlock.withCtx(2, memoryContext);
        this.state = new GlState();
        this.fbo = new Fbo.Builder(memoryContext)
                .withColorAttachment(600, 400)
                .withStencilDepthAttachment(600, 400)
                .build();
    }

    public MatricesUniformBlock getMatricesBlock() {
        return matricesBlock;
    }

    public MemoryContext getMemoryContext() {
        return memoryContext;
    }

    public LightUniformBlock getLightBlock() {
        return lightBlock;
    }

    public GlState getState() {
        return state;
    }

    @Override
    public void close() {
        matricesBlock.close();
        lightBlock.close();
        memoryContext.close();
        fbo.close();
    }

    public MaterialUniformBlock getMaterialBlock() {
        return materialBlock;
    }

    public void setMaterialBlock(MaterialUniformBlock materialBlock) {
        this.materialBlock = materialBlock;
    }

    public Fbo getFbo() {
        return fbo;
    }
}

package com.theopus.xengine.core.render;

import com.theopus.xengine.wrapper.opengl.GlState;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.shader.LightUniformBlock;
import com.theopus.xengine.wrapper.opengl.shader.MatricesUniformBlock;

import java.io.Closeable;

public class GLContext implements Closeable {
    private LightUniformBlock lightBlock;
    private MatricesUniformBlock matricesBlock;
    private MemoryContext memoryContext;
    private GlState state;

    public GLContext() {
        this.memoryContext = new MemoryContext();
        this.matricesBlock = MatricesUniformBlock.withCtx(0, memoryContext);
        this.lightBlock = LightUniformBlock.withCtx(1, memoryContext);
        this.state = new GlState();

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
        memoryContext.close();
    }
}

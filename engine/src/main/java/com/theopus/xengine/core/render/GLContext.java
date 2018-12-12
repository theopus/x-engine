package com.theopus.xengine.core.render;

import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.shader.MatricesUniformBlock;

import java.io.Closeable;
import java.io.IOException;

public class GLContext implements Closeable {
    private MatricesUniformBlock matricesBlock;
    private MemoryContext memoryContext;

    public GLContext() {
        this.memoryContext = new MemoryContext();
        this.matricesBlock = MatricesUniformBlock.withCtx(0, memoryContext);

    }

    public MatricesUniformBlock getMatricesBlock() {
        return matricesBlock;
    }

    public MemoryContext getMemoryContext() {
        return memoryContext;
    }

    @Override
    public void close() {
        matricesBlock.close();
        memoryContext.close();
    }
}

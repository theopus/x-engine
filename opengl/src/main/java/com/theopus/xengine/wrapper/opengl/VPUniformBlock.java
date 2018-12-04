package com.theopus.xengine.wrapper.opengl;

import com.google.common.base.Preconditions;
import com.theopus.xengine.wrapper.opengl.buffers.Ubo;
import com.theopus.xengine.wrapper.opengl.utils.Buffers;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;

/**
 * expected to be:
 *
 *
 * layout (std140) uniform Matrices
 * {
 *     mat4 view;
 *     mat4 projection;
 * };
 */
public class VPUniformBlock extends UniformBlock {

    private static final int SIZE = MemorySizeConstants.MAT4_FLOAT * 2;
    private static final String NAME = "Matrices";
    private final FloatBuffer viewMtxBuffer;
    private final FloatBuffer projMtxBuffer;

    public VPUniformBlock(int bindingPoint, Ubo ubo) {
        super(bindingPoint, NAME, ubo);
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        this.viewMtxBuffer = MemoryUtil.memAllocFloat(MemorySizeConstants.MAT4_FLOAT);
        this.projMtxBuffer = MemoryUtil.memAllocFloat(MemorySizeConstants.MAT4_FLOAT);
    }

    public VPUniformBlock(int bindingPoint) {
        super(bindingPoint, NAME, new Ubo(SIZE, GL15.GL_STATIC_DRAW));
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        this.viewMtxBuffer = MemoryUtil.memAllocFloat(MemorySizeConstants.MAT4_FLOAT);
        this.projMtxBuffer = MemoryUtil.memAllocFloat(MemorySizeConstants.MAT4_FLOAT);
    }

    public void loadViewMatrix(Matrix4f view){
        Buffers.put(view, viewMtxBuffer);
        ubo.bufferSubData(0, viewMtxBuffer);
        viewMtxBuffer.clear();
    }

    public void loadProjectionMatrix(Matrix4f projection){
        projMtxBuffer.clear();
        projection.get(projMtxBuffer);
//        ubo.bufferSubData(MemorySizeConstants.MAT4_FLOAT, projMtxBuffer);
    }

    @Override
    public void close() throws IOException {
        MemoryUtil.memFree(viewMtxBuffer);
        MemoryUtil.memFree(projMtxBuffer);
    }
}

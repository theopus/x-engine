package com.theopus.xengine.wrapper.opengl.shader;

import com.google.common.base.Preconditions;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.MemorySizeConstants;
import com.theopus.xengine.wrapper.opengl.objects.Ubo;
import com.theopus.xengine.wrapper.opengl.utils.Buffers;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

/**
 * expected to be:
 * <p>
 * <p>
 * layout (std140) uniform Matrices
 * {
 * mat4 view;
 * mat4 projection;
 * };
 */
public class MatricesUniformBlock extends UniformBlock {

    public static final int SIZE = MemorySizeConstants.MAT4_FLOAT * 2;
    public static final String NAME = "Matrices";

    private final FloatBuffer viewMtxBuffer;
    private final FloatBuffer projMtxBuffer;

    public MatricesUniformBlock(int bindingPoint, Ubo ubo) {
        super(bindingPoint, NAME, ubo);
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        this.viewMtxBuffer = MemoryUtil.memAllocFloat(GlDataType.MAT4_FLOAT.size);
        this.projMtxBuffer = MemoryUtil.memAllocFloat(GlDataType.MAT4_FLOAT.size);
        bindToIndex();
    }

    public MatricesUniformBlock(int bindingPoint) {
        super(bindingPoint, NAME, new Ubo(SIZE, GL15.GL_STATIC_DRAW));
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        this.viewMtxBuffer = MemoryUtil.memAllocFloat(GlDataType.MAT4_FLOAT.size);
        this.projMtxBuffer = MemoryUtil.memAllocFloat(GlDataType.MAT4_FLOAT.size);
        //TODO: [INVESTIGATE] Works fine (W10) bind before shader assignment
        bindToIndex();
    }

    public static MatricesUniformBlock withCtx(int bindingPoint, MemoryContext ctx) {
        Ubo ubo = new Ubo(SIZE, GL15.GL_STATIC_DRAW);
        ctx.put(ubo);
        return new MatricesUniformBlock(bindingPoint, ubo);
    }

    public void loadViewMatrix(Matrix4f view) {
        Buffers.put(view, viewMtxBuffer);
        ubo.bufferSubData(0, viewMtxBuffer);
        viewMtxBuffer.clear();
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        Buffers.put(projection, projMtxBuffer);
        ubo.bufferSubData(MemorySizeConstants.MAT4_FLOAT, projMtxBuffer);
        projMtxBuffer.clear();
    }

    @Override
    public void close() {
        MemoryUtil.memFree(viewMtxBuffer);
        MemoryUtil.memFree(projMtxBuffer);
    }
}

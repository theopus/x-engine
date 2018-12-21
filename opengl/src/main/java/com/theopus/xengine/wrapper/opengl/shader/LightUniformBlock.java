package com.theopus.xengine.wrapper.opengl.shader;

import com.google.common.base.Preconditions;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.objects.Ubo;
import com.theopus.xengine.wrapper.opengl.utils.Buffers;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import com.theopus.xengine.wrapper.utils.State;
import org.joml.Matrix4f;
import org.joml.Vector3f;
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
public class LightUniformBlock extends UniformBlock {

    public static final int SIZE = GlDataType.VEC4_FLOAT.byteSize() * 3;
    public static final String NAME = "Light";

    private static int COLOR_OFFSET = 0;
    private static int POSITION_OFFSET = GlDataType.VEC4_FLOAT.byteSize();
    private static int REFLECTIVITY_OFFSET = GlDataType.VEC4_FLOAT.byteSize() * 2;
    private static int AMBIENT_LEVEL_OFFSET = GlDataType.VEC4_FLOAT.byteSize() * 3;

    private FloatBuffer vector3fBuffer;

    public LightUniformBlock(int bindingPoint, Ubo ubo) {
        super(bindingPoint, NAME, ubo);
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        this.vector3fBuffer = MemoryUtil.memAllocFloat(GlDataType.VEC4_FLOAT.size);
        bindToIndex();
        init();
    }

    public LightUniformBlock(int bindingPoint) {
        super(bindingPoint, NAME, new Ubo(SIZE, GL15.GL_STATIC_DRAW));
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        //TODO: [INVESTIGATE] Works fine (W10) bind before shader assignment
        this.vector3fBuffer = MemoryUtil.memAllocFloat(GlDataType.VEC4_FLOAT.size);
        bindToIndex();
        init();
    }

    public void init(){
        loadPosition(new Vector3f(1,1,1));
        loadColor(new Vector3f(1,1,1));
        loadReflectivity(new Vector3f(1,1,1));
    }

    public static LightUniformBlock withCtx(int bindingPoint, MemoryContext ctx) {
        Ubo ubo = new Ubo(SIZE, GL15.GL_STATIC_DRAW);
        ctx.put(ubo);
        return new LightUniformBlock(bindingPoint, ubo);
    }

    public void loadColor(Vector3f light) {
        Buffers.put(light, vector3fBuffer);
        ubo.bufferSubData(COLOR_OFFSET, vector3fBuffer);
        vector3fBuffer.clear();
    }

    public void loadPosition(Vector3f pos) {
        Buffers.put(pos, vector3fBuffer);
        ubo.bufferSubData(POSITION_OFFSET, vector3fBuffer);
        vector3fBuffer.clear();
    }

    public void loadReflectivity(Vector3f ref) {
        Buffers.put(ref, vector3fBuffer);
        ubo.bufferSubData(REFLECTIVITY_OFFSET, vector3fBuffer);
        vector3fBuffer.clear();
    }

    @Override
    public void close() {
        MemoryUtil.memFree(vector3fBuffer);
    }
}

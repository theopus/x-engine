package com.theopus.xengine.wrapper.opengl.shader.ubos;

import java.nio.FloatBuffer;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import com.google.common.base.Preconditions;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.objects.Ubo;
import com.theopus.xengine.wrapper.opengl.utils.Buffers;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import com.theopus.xengine.wrapper.utils.State;

/**
 * expected to be:
 * <p>
 * <p>
 * layout (std140) uniform Light
 * {
 * vec3 position;
 * vec3 intensity;
 * };
 */
public class LightUniformBlock extends UniformBlock {

    public static final int SIZE = GlDataType.VEC4_FLOAT.byteSize * 2;
    public static final String NAME = "Light";

    private static int POSITION_OFFSET = 0;
    private static int INTENSITY_OFFSET = GlDataType.VEC4_FLOAT.byteSize;

    private FloatBuffer vector3fBuffer;

    private State<Vector3f> position;
    private State<Vector3f> intensity;

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

    public static LightUniformBlock withCtx(int bindingPoint, MemoryContext ctx) {
        Ubo ubo = new Ubo(SIZE, GL15.GL_STATIC_DRAW);
        ctx.put(ubo);
        return new LightUniformBlock(bindingPoint, ubo);
    }

    public void init() {
        position = State.v3(new Vector3f(1, 1, 1), position -> {
            Buffers.put(position, vector3fBuffer);
            ubo.bufferSubData(POSITION_OFFSET, vector3fBuffer);
            vector3fBuffer.clear();
        });

        intensity = State.v3(new Vector3f(1, 1, 1), diffuse -> {
            Buffers.put(diffuse, vector3fBuffer);
            ubo.bufferSubData(INTENSITY_OFFSET, vector3fBuffer);
            vector3fBuffer.clear();
        });
    }

    public void loadPosition(Vector3f pos) {
        position.update(pos);
    }

    public void loadInensity(Vector3f light) {
        intensity.update(light);
    }


    @Override
    public void close() {
        MemoryUtil.memFree(vector3fBuffer);
    }
}

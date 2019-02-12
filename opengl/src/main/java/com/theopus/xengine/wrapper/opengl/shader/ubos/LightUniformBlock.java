package com.theopus.xengine.wrapper.opengl.shader.ubos;

import com.google.common.base.Preconditions;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.objects.Ubo;
import com.theopus.xengine.wrapper.opengl.utils.Buffers;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import com.theopus.xengine.wrapper.utils.State;
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

    public static final int SIZE = GlDataType.VEC4_FLOAT.byteSize * 3;
    public static final String NAME = "Light";

    private static int POSITION_OFFSET = 0;
    private static int DIFFUSE_INTENSITY = GlDataType.VEC4_FLOAT.byteSize;
    private static int AMBIENT_INTENSITY = GlDataType.VEC4_FLOAT.byteSize * 2;
    private static int SPECULAR_INTENSITY = GlDataType.VEC4_FLOAT.byteSize * 3;

    private FloatBuffer vector3fBuffer;

    private State<Vector3f> position;
    private State<Vector3f> diffuse;

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
        position = State.v3(new Vector3f(1,1,1), position -> {
            Buffers.put(position, vector3fBuffer);
            ubo.bufferSubData(POSITION_OFFSET, vector3fBuffer);
            vector3fBuffer.clear();
        });

        diffuse = State.v3(new Vector3f(1,1,1), diffuse ->{
            Buffers.put(diffuse, vector3fBuffer);
            ubo.bufferSubData(DIFFUSE_INTENSITY, vector3fBuffer);
            vector3fBuffer.clear();
        });

        loadAmbient(new Vector3f(1,1,1));
        loadSpecular(new Vector3f(1,1,1));
    }

    public static LightUniformBlock withCtx(int bindingPoint, MemoryContext ctx) {
        Ubo ubo = new Ubo(SIZE, GL15.GL_STATIC_DRAW);
        ctx.put(ubo);
        return new LightUniformBlock(bindingPoint, ubo);
    }

    public void loadPosition(Vector3f pos) {
        position.update(pos);
    }

    public void loadDiffuse(Vector3f light) {
        diffuse.update(light);
    }

    public void loadAmbient(Vector3f ref) {
        Buffers.put(ref, vector3fBuffer);
        ubo.bufferSubData(AMBIENT_INTENSITY, vector3fBuffer);
        vector3fBuffer.clear();
    }

    public void loadSpecular(Vector3f specular){
        Buffers.put(specular, vector3fBuffer);
        ubo.bufferSubData(SPECULAR_INTENSITY, vector3fBuffer);
        vector3fBuffer.clear();
    }

    @Override
    public void close() {
        MemoryUtil.memFree(vector3fBuffer);
    }
}

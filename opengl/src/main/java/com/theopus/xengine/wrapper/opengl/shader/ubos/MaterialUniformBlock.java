package com.theopus.xengine.wrapper.opengl.shader.ubos;

import com.google.common.base.Preconditions;
import com.theopus.xengine.wrapper.opengl.MemoryContext;
import com.theopus.xengine.wrapper.opengl.objects.Material;
import com.theopus.xengine.wrapper.opengl.objects.Ubo;
import com.theopus.xengine.wrapper.opengl.shader.Uniforms;
import com.theopus.xengine.wrapper.opengl.utils.Buffers;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL15;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.FloatBuffer;

public class MaterialUniformBlock extends UniformBlock {

    public static final int SIZE = GlDataType.FLOAT.byteSize() * 4;
    public static final String NAME = "Material";

    private static int AMBIENT_REF_OFFSET = 0;
    private static int DIFFUSE_REF_OFFSET = GlDataType.FLOAT.byteSize();
    private static int SPECULAR_REF_OFFSET = GlDataType.FLOAT.byteSize() * 2;
    private static int SHININESS_OFFSET = GlDataType.FLOAT.byteSize() * 3;

    private FloatBuffer vector4fBuffer;
    private Vector4f v4 = new Vector4f();

    public MaterialUniformBlock(int bindingPoint, Ubo ubo) {
        super(bindingPoint, NAME, ubo);
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        this.vector4fBuffer = MemoryUtil.memAllocFloat(GlDataType.VEC4_FLOAT.size);
        bindToIndex();
        init();
    }

    public MaterialUniformBlock(int bindingPoint) {
        super(bindingPoint, NAME, new Ubo(SIZE, GL15.GL_STATIC_DRAW));
        Preconditions.checkArgument(SIZE == ubo.getSize(), "UBO SIZE should be " + SIZE);
        //TODO: [INVESTIGATE] Works fine (W10) bind before shader assignment
        this.vector4fBuffer = MemoryUtil.memAllocFloat(GlDataType.VEC4_FLOAT.size);
        bindToIndex();
        init();
    }

    public void init(){
        loadMaterial(new Material(1,1,1,1));
    }

    public void loadMaterial(Material material){
        v4.x = material.ambientReflectivity;
        v4.y = material.diffuseReflectivity;
        v4.z = material.specularReflectivity;
        v4.w = material.shininess;
        Buffers.put(v4, vector4fBuffer);
        ubo.bufferSubData(0, vector4fBuffer);
    }

    public static MaterialUniformBlock withCtx(int bindingPoint, MemoryContext memoryContext) {
        Ubo ubo = new Ubo(SIZE, GL15.GL_STATIC_DRAW);
        memoryContext.put(ubo);
        return new MaterialUniformBlock(bindingPoint, ubo);
    }

    @Override
    public void close() throws IOException {
        MemoryUtil.memFree(vector4fBuffer);
    }
}

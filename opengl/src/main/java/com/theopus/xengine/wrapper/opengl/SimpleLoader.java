package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.GlDataType;
import com.theopus.xengine.wrapper.opengl.objects.*;
import org.lwjgl.opengl.GL15;

import java.util.Arrays;
import java.util.List;

public class SimpleLoader extends Loader {

    public SimpleLoader(MemoryContext context) {
        super(context);
    }

    public Vao load(float[] positions, int[] indexes) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);
        Vbo vbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Attribute posAttr = Attribute
                .singleVboAttribute(0, GlDataType.FLOAT, vbo, 3);

        context.put(ebo, vbo);
        return new Vao(ebo, posAttr);
    }

    public TexturedVao load(float[] positions, float[] uvs, int[] indexes, String path) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);

        float[] mixed = mixPositionsAndUv(positions, uvs);
        Vbo posUv = new Vbo(mixed, GL15.GL_STATIC_DRAW);

//        Attribute posAttr = Attribute.sharedVboAttribute(0, GL15.GL_FLOAT, posUv, 3, MemorySizeConstants.VEC3_FLOAT + MemorySizeConstants.VEC2_FLOAT, 0);
//        Attribute uvAttr = Attribute.sharedVboAttribute(1, GL15.GL_FLOAT, posUv, 2, MemorySizeConstants.VEC3_FLOAT + MemorySizeConstants.VEC2_FLOAT, MemorySizeConstants.VEC3_FLOAT);

//        new Attribute.SharedAttributeBuilder()
//                .add()
//        Vbo pos = new Vbo(positions, GL15.GL_STATIC_DRAW);
//        Vbo uv = new Vbo(uvs, GL15.GL_STATIC_DRAW);
//
//        Attribute posAttr = Attribute
//                .singleVboAttribute(0, GlDataType.FLOAT, pos, 3);
//
//        Attribute uvAttr = Attribute
//                .singleVboAttribute(1, GlDataType.FLOAT, uv, 2);

        List<Attribute> build = new Attribute.SharedAttributeBuilder(posUv)
                .add(GlDataType.VEC3_FLOAT)
                .add(GlDataType.VEC2_FLOAT)
                .build();

        Vao vao = new Vao(ebo, build.toArray(new Attribute[0]));
        Texture texture = Texture.loadTexture(path);

        context.put(ebo, posUv);
        context.put(texture);

        System.out.println(Arrays.toString(mixPositionsAndUv(positions, uvs)));
        return new TexturedVao(vao, texture);
    }

    private float[] mixPositionsAndUv(float[] positions, float[] uv) {
        int posPointer = 0;
        int uvPointer = 0;
        int resPointer = 0;

        float[] result = new float[positions.length + uv.length];

        while (posPointer < positions.length) {
            result[resPointer++] = positions[posPointer++];
            result[resPointer++] = positions[posPointer++];
            result[resPointer++] = positions[posPointer++];

            result[resPointer++] = uv[uvPointer++];
            result[resPointer++] = uv[uvPointer++];
        }

        return result;
    }


}

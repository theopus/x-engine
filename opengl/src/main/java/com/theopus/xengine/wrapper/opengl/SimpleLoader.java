package com.theopus.xengine.wrapper.opengl;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL15;

import com.theopus.xengine.wrapper.opengl.objects.Attribute;
import com.theopus.xengine.wrapper.opengl.objects.Ebo;
import com.theopus.xengine.wrapper.opengl.objects.Material;
import com.theopus.xengine.wrapper.opengl.objects.MaterialVao;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.objects.Vao;
import com.theopus.xengine.wrapper.opengl.objects.Vbo;
import com.theopus.xengine.wrapper.opengl.utils.GlDataType;
import com.theopus.xengine.wrapper.utils.ObjParser;

public class SimpleLoader extends Loader {

    public SimpleLoader(MemoryContext context) {
        super(context);
    }

    public Vao load(float[] positions, int[] indexes) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);
        Vbo vbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Attribute posAttr = Attribute
                .singleVboAttribute(0, GlDataType.VEC3_FLOAT, vbo);

        context.put(ebo, vbo);
        return new Vao(ebo, posAttr);
    }

    public TexturedVao load(float[] positions, float[] uvs, int[] indexes, String path) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);

        float[] mixed = mixPositionsAndUv(positions, uvs);
        Vbo posUv = new Vbo(mixed, GL15.GL_STATIC_DRAW);

        List<Attribute> build = new Attribute.SharedAttributeBuilder(posUv)
                .add(GlDataType.VEC3_FLOAT)
                .add(GlDataType.VEC2_FLOAT)
                .build();

        Vao vao = new Vao(ebo, build.toArray(new Attribute[0]));
        Texture texture = Texture.loadTexture(path);

        context.put(ebo, posUv);
        context.put(texture);

        return new TexturedVao(vao, texture);
    }

    public TexturedVao load(float[] positions, float[] uvs, float[] normals, int[] indexes, String texture) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);
        Vbo posVbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Vbo uvVbo = new Vbo(uvs, GL15.GL_STATIC_DRAW);
        Vbo normalVbo = new Vbo(normals, GL15.GL_STATIC_DRAW);

        Attribute posAttr = Attribute.singleVboAttribute(0, GlDataType.VEC3_FLOAT, posVbo);
        Attribute uvAttr = Attribute.singleVboAttribute(1, GlDataType.VEC2_FLOAT, uvVbo);
        Attribute normalAttr = Attribute.singleVboAttribute(2, GlDataType.VEC3_FLOAT, normalVbo);

        Vao vao = new Vao(ebo, posAttr, uvAttr, normalAttr);
        Texture t = Texture.loadTexture(texture);
        return new TexturedVao(vao, t);
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

    public TexturedVao load(String obj) {
        ObjParser.Result parse;
        try {
            parse = ObjParser.parse(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return load(parse.getPosArr(), parse.getTextCoordArr(), parse.getNormArr(), parse.getIndicesArr(), "textures/white.png");
    }

    public MaterialVao load(String obj, Material material) {
        return new MaterialVao(load(obj), material);
    }

    public TexturedVao loadText(float[] positions, float[] uvs, Texture texture) {
        System.out.println(Arrays.toString(positions));
        System.out.println(Arrays.toString(uvs));
        Vbo posVbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Vbo uvVbo = new Vbo(uvs, GL15.GL_STATIC_DRAW);

        Attribute posAttr = Attribute.singleVboAttribute(0, GlDataType.VEC2_FLOAT, posVbo);
        Attribute uvAttr = Attribute.singleVboAttribute(1, GlDataType.VEC2_FLOAT, uvVbo);

        Vao vao = new Vao(positions.length / 2, posAttr, uvAttr);
        return new TexturedVao(vao, texture);
    }
}

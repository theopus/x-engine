package com.theopus.xengine.wrapper.opengl;

import com.theopus.xengine.wrapper.opengl.buffers.Attribute;
import com.theopus.xengine.wrapper.opengl.buffers.Ebo;
import com.theopus.xengine.wrapper.opengl.buffers.Vao;
import com.theopus.xengine.wrapper.opengl.buffers.Vbo;
import org.lwjgl.opengl.GL15;

public class SimpleLoader extends Loader {
//    public Vao loadSimpleVao(float[] positions, int[] indexes) {
//        int vao = createVAO();
//        bindVao(vao);
//        int eboId = bindIndicesBuffer(indexes);
//        int posVbo = writeInVao(0, 3, positions);
//        //
//        GL30.glEnableVertexAttribArray(0);
//        unbindVao();
//        unbindIndicesBuffer();
//
//        Vbo vbo = new Vbo(posVbo, GL15.GL_ARRAY_BUFFER, MemorySizeConstants.VEC3_FLOAT * positions.length, GL15.GL_STATIC_DRAW);
//        Vbo ebo = new Vbo(eboId, GL15.GL_ELEMENT_ARRAY_BUFFER, MemorySizeConstants.INT * indexes.length, GL15.GL_STATIC_DRAW);
//
//        Attribute posAttr = new Attribute("positions", 0, 3);
//
//
//        return new VaoBuilder()
//                .setAttributes(posAttr)
//                .setId(vao)
//                .setVertexes(indexes.length)
//                .setVbos(vbo)
//                .setEbo(ebo)
//                .createVao();
//    }

    public Vao loadSimpleVao(float[] positions, int[] indexes) {
        Ebo ebo = new Ebo(indexes, GL15.GL_STATIC_DRAW);
        Vbo vbo = new Vbo(positions, GL15.GL_STATIC_DRAW);
        Attribute posAttr = Attribute
                .singleVboAttribute(0, GL15.GL_FLOAT, vbo, 3, MemorySizeConstants.FLOAT);

        return new Vao(ebo, posAttr);
    }


}

package com.theopus.client.render;

import com.theopus.xengine.opengl.SimpleLoader;
import com.theopus.xengine.opengl.Vao;
import com.theopus.xengine.opengl.shader.StaticShader;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.utils.BitSetUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Ver0ModuleImpl implements Ver0Module {

    private static int counter = 0;
    private final SimpleLoader loader;
    private Map<Integer, Vao> models = new HashMap<>();
    private Map<Integer, BitSet> entityMap = new HashMap<>();

    private StaticShader shader;

    public Ver0ModuleImpl() {
        try {
            shader = new StaticShader("static.vert", "static.frag");
            loader = new SimpleLoader();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int load(Ver0Model ver0Model) {
        Vao vao = loader.loadSimpleVao(ver0Model.getVertexes(), ver0Model.getIndexes());
        models.put(counter, vao);
        return counter++;
    }

    @Override
    public void bind(int entityId, int model) {
        if (models.keySet().contains(model)) {
            BitSet set = entityMap.get(model);
            if (set == null) {
                set = new BitSet();
                entityMap.put(model, set);
            }
            set.set(entityId);
        } else {
            //TODO
        }
    }


    @Override
    public void prepare(int modelId) {
        shader.bind();
        GL30.glBindVertexArray(models.get(modelId).getId());
    }

    @Override
    public void finish(int modelId) {
        GL30.glBindVertexArray(0);
        shader.unbind();
    }

    @Override
    public void render(WorldPositionTrait trait) {
    }

    private void render(Vao vao, WorldPositionTrait trait) {
        shader.transformation().load(
                trait.getTransformation()
        );
        GL30.glDrawElements(GL11.GL_TRIANGLES, vao.getLength(), GL30.GL_UNSIGNED_INT, 0);
    }

    @Override
    public void render(BitSet entities, EntityManager em) {
        BitSet bitSet = new BitSet();
        bitSet.or(entities);
        TraitMapper<WorldPositionTrait> mapper = em.getMapper(WorldPositionTrait.class);

        for (Map.Entry<Integer, BitSet> modelEntities : entityMap.entrySet()) {
            BitSet entitiesValue = modelEntities.getValue();
            Integer modelId = modelEntities.getKey();
            if (bitSet.intersects(entitiesValue)) {
                BitSet cross = BitSetUtils.cross(bitSet, entitiesValue);
                prepare(modelId);
                Vao vao = models.get(modelId);
                for (int i = cross.nextSetBit(0); i >= 0; i = cross.nextSetBit(i + 1)) {
                    render(vao, mapper.get(i));
                }
                finish(modelId);
                bitSet.xor(cross);
            }

        }


    }

    @Override
    public void close() {
        loader.close();
        shader.cleanup();
    }


}

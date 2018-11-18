package com.theopus.client.render;

import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.ecs.mapper.ViewEntityManager;
import com.theopus.xengine.opengl.DefaultRenderCommand;
import com.theopus.xengine.opengl.SimpleLoader;
import com.theopus.xengine.opengl.Vao;
import com.theopus.xengine.opengl.shader.StaticShader;
import com.theopus.xengine.utils.BitSetUtils;
import org.joml.Matrix4f;

import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class Ver0ModuleImpl implements Ver0Module {

    private static int counter = 0;
    private final SimpleLoader loader;
    private Map<Integer, Vao> models = new HashMap<>();
    private Map<Integer, BitSet> entityMap = new HashMap<>();

    private DefaultRenderCommand command;

    public Ver0ModuleImpl() {
        try {
            command = new DefaultRenderCommand(new StaticShader("static.vert", "static.frag"));
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
        command.prepare(models.get(modelId));
    }

    @Override
    public void finish(int modelId) {
        command.finish();
    }


    private void render(Vao vao, WorldPositionTrait trait) {
        command.render(vao, trait.getTransformation());
    }

    @Override
    public void render(BitSet entities) {
        BitSet bitSet = new BitSet();
        bitSet.or(entities);

    }

    @Override
    public void close() {
        loader.close();
        command.close();
    }

    @Override
    public void render(BitSet entities, ViewEntityManager em) {
        for (Map.Entry<Integer, BitSet> modelEntities : entityMap.entrySet()) {
            BitSet entitiesValue = modelEntities.getValue();
            Integer modelId = modelEntities.getKey();
            if (entities.intersects(entitiesValue)) {
                BitSet cross = BitSetUtils.cross(entities, entitiesValue);
                prepare(modelId);
                Vao vao = models.get(modelId);
                for (int i = cross.nextSetBit(0); i >= 0; i = cross.nextSetBit(i + 1)) {
                    render(vao, em.get(i, WorldPositionTrait.class));
                }
                finish(modelId);
                entities.xor(cross);
            }
        }
    }

    @Override
    public void loadView(Matrix4f view) {
        command.shader.bind();
        command.loadView(view);
        command.shader.unbind();
    }

    @Override
    public void loadProjection(Matrix4f projection) {
        command.shader.bind();
        command.loadProjection(projection);
        command.shader.unbind();
    }
}

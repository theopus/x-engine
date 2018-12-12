package com.theopus.xengine.core.render;

import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;

import java.util.HashMap;
import java.util.Map;

public abstract class ArtemisRenderModule<T, D> implements RenderModule<T> {

    private final Map<String, D> groupMap;
    private final String prefix;

    @Wire
    private CustomGroupManager groupManager;
    private int count = 0;

    public ArtemisRenderModule() {
        this.prefix = this.getClass().getSimpleName() + "_";
        groupMap = new HashMap<>();
    }

    public ArtemisRenderModule(String prefix) {
        this.prefix = prefix;
        groupMap = new HashMap<>();
    }

    @Override
    public String load(T t) {
        String group = prefix + count++;
        groupMap.put(group, loadModel(t));
        return group;
    }

    @Override
    public void bind(String model, int entityId) {
        if (groupMap.keySet().contains(model)) {
            groupManager.add(entityId, model);
        } else {
            throw new RuntimeException("Not found model: " + model);
        }
    }

    @Override
    public void render() {
        for (Map.Entry<String, D> stringD : groupMap.entrySet()) {
            String group = stringD.getKey();
            D D = stringD.getValue();
            ImmutableBag<Entity> entities = groupManager.getEntities(group);
            prepareModel(D);
            for (int i = 0; i < entities.size(); i++) {
                int id = entities.get(i).getId();
                renderModel(id, D);
            }
            finishModel(D);
        }
    }

    public String getPrefix() {
        return prefix;
    }

    @Override
    public void prepare() {

    }

    @Override
    public void finish() {

    }

    public abstract void renderModel(int entityId, D d);

    public abstract D loadModel(T d);

    public abstract void prepareModel(D d);

    public abstract void finishModel(D d);

}

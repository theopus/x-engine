package com.theopus.xengine.core.render;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.artemis.ArchetypeBuilder;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.utils.ImmutableBag;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;

public abstract class ArtemisRenderModule<Load, Instance> implements RenderModule<Load> {

    private final Map<String, Instance> groupMap;
    private final String prefix;

    @Wire(injectInherited = true)
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
    public String loadToModule(Load load) {
        String group = prefix + count++;
        return loadToModule(group, load);
    }

    @Override
    public String loadToModule(String title, Load load) {
        groupMap.put(title, load(load));
        return title;
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
    public void renderModule() {
        for (Map.Entry<String, Instance> stringD : groupMap.entrySet()) {
            String group = stringD.getKey();
            Instance Instance = stringD.getValue();
            ImmutableBag<Entity> entities = groupManager.getEntities(group);
            prepare(Instance);
            for (int i = 0; i < entities.size(); i++) {
                int id = entities.get(i).getId();
                render(id, Instance);
            }
            finish(Instance);
        }


    }

    @Override
    public Map<String, ImmutableBag<Entity>> bindings() {
        return models().stream().collect(Collectors.toMap(Function.identity(), m -> groupManager.getEntities(m)));
    }

    @Override
    public Set<String> models() {
        return groupMap.keySet();
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    public Instance get(String prefix) {
        return groupMap.get(prefix);
    }

    @Override
    public void prepareModule() {

    }

    public abstract ArchetypeBuilder components();

    @Override
    public void finishModule() {

    }

    @Override
    public void init() {

    }

    public abstract void render(int entityId, Instance instance);

    public abstract Instance load(Load d);

    public abstract void prepare(Instance instance);

    public abstract void finish(Instance instance);

}

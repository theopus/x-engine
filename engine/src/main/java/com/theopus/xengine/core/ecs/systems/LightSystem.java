package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalIteratingSystem;
import com.theopus.xengine.core.ecs.components.Light;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.utils.OpsCounter;

public class LightSystem extends IntervalIteratingSystem {

    private final OpsCounter counter = new OpsCounter("Light");

    @Wire(name = "renderer")
    private BaseRenderer renderer;

    private ComponentMapper<Light> lMapper;
    private ComponentMapper<Transformation> pMapper;

    public LightSystem() {
        super(Aspect.all(Light.class), 100);
    }

    @Override
    protected void process(int entityId) {
        Light light = lMapper.get(entityId);
        Transformation transformation = pMapper.get(entityId);
        renderer.loadLight(light.diffuse, transformation.position);
    }
}

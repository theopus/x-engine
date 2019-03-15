package com.theopus.xengine.core.ecs.systems.scipting;

import com.artemis.World;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.EntityFactory;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.render.BaseRenderer;

public class ExecutingEngineContext {

    @Wire
    public World world;

    @Wire(name = "renderer")
    public BaseRenderer renderer;

    @Wire
    public EntityFactory factory;
    @Wire
    public EventBus eventBus;
}

package com.theopus.xengine.core.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.utils.EntityBuilder;
import com.artemis.utils.IntBag;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.ecs.components.Render;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.utils.OpsCounter;

public class RenderSystem extends BaseSystem {

    private final OpsCounter counter = new OpsCounter("Render");

    @Wire(name = "renderer")
    private BaseRenderer renderer;

    @Override
    protected void processSystem() {
        renderer.clearBuffer();
        renderer.render();

        counter.operateAndLog();
    }
}

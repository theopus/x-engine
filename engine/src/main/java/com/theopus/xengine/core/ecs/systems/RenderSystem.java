package com.theopus.xengine.core.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
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

//        counter.operateAndLog();
    }
}

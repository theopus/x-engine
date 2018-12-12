package com.theopus.xengine.core.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.Subscriber;
import com.theopus.xengine.core.platform.FramebufferEvent;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.utils.Maths;
import org.joml.Matrix4f;

public class ProjectionSystem extends BaseSystem implements Subscriber<FramebufferEvent> {

    @Wire(name = "renderer")
    private BaseRenderer renderer;

    @Override
    protected void processSystem() {
    }

    @Override
    public void onEvent(FramebufferEvent framebufferEvent) {
        Matrix4f projection = Maths
                .createProjectionMatrixAuto((float) Math.toRadians(70), 0.1f, 1000f,
                        framebufferEvent.width * 1f / framebufferEvent.height);
        renderer.loadFramebufferSize(framebufferEvent.width, framebufferEvent.height);
        renderer.loadProjectionMatrix(projection);
    }
}

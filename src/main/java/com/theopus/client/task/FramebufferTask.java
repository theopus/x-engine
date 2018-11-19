package com.theopus.client.task;

import com.theopus.xengine.event.EventManager;
import com.theopus.xengine.event.TopicReader;
import com.theopus.xengine.inject.Event;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.render.Render;
import com.theopus.xengine.utils.Maths;
import org.joml.Vector2i;

public class FramebufferTask extends ComponentTask {

    private final Render render;

    @Event(topicId = EventManager.Topics.FRAMEBUFFER_CHANGED, listener = true)
    private TopicReader<Vector2i> reader;

    @Inject
    public FramebufferTask(Render render) {
        super(Context.MAIN, false, 60);
        this.render = render;
    }

    @Override
    public void process() {
        reader.read().forEach(v2 -> {
            System.out.println(v2);
            render.loadViewPort(v2.data().x, v2.data().y);
            render.loadProjection(Maths.createProjectionMatrixAuto(
                    90, 0.1f, 1000, v2.data().x * 1.0f / v2.data().y
            ));
        });
    }
}

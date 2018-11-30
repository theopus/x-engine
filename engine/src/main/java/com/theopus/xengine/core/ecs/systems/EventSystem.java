package com.theopus.xengine.core.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.EventBus;

public class EventSystem extends BaseSystem {
    @Wire
    private EventBus eventBus;

    @Override
    protected void processSystem() {
        eventBus.propagate();
    }
}

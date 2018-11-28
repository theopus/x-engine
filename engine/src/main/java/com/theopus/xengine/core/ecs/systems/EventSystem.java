package com.theopus.xengine.core.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.events.Subscriber;
import com.theopus.xengine.core.events.VoidEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventSystem extends BaseSystem implements Subscriber<VoidEvent> {

    @Wire
    private EventBus eventBus;

    public EventSystem() {

    }

    @Override
    protected void processSystem() {
        eventBus.propagate();
    }

    @Override
    public void onEvent(VoidEvent voidEvent) {
        System.out.println(voidEvent);
    }
}

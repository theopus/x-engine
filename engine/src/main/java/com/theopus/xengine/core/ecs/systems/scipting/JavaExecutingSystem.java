package com.theopus.xengine.core.ecs.systems.scipting;

import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.Subscriber;

public class JavaExecutingSystem extends ExecutingEngineSystem implements Subscriber<JavaExecutionEvent> {

    @Wire
    private ExecutingEngineContext context;

    @Override
    public void onEvent(JavaExecutionEvent event) {
        event.action.accept(context);
    }
}

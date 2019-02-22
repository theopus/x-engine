package com.theopus.xengine.core.ecs.systems.scipting;

import java.util.function.Consumer;

public class JavaExecutionEvent {

    public final Consumer<ExecutingEngineContext> action;

    public JavaExecutionEvent(Consumer<ExecutingEngineContext> action) {
        this.action = action;
    }
}

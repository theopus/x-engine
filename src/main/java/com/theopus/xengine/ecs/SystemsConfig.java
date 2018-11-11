package com.theopus.xengine.ecs;

import com.theopus.xengine.ecs.system.BaseSystem;

public class SystemsConfig {
    private Class<? extends BaseSystem>[] systems;

    @SafeVarargs
    public SystemsConfig(Class<? extends BaseSystem>... systems) {
        this.systems = systems;
    }

    public Class<? extends BaseSystem>[] getSystems() {
        return systems;
    }
}

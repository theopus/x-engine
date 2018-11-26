package com.theopus.xengine.core.ecs.systems;

import com.artemis.Aspect;
import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;
import com.artemis.systems.IteratingSystem;

public class RenderSystem extends BaseSystem {

    @Wire
    private GroupManager groupManager;

    @Override
    protected void processSystem() {
//        groupManager.getEntities()
    }
}

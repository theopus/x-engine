package com.theopus.xengine.core.ecs.managers;

import com.artemis.managers.GroupManager;

public class CustomGroupManager extends GroupManager {

    public void add(int entity, String group) {
        super.add(getWorld().getEntity(entity), group);
    }
}

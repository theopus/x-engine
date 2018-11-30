package com.theopus.xengine.core.ecs.managers;

import com.artemis.Entity;
import com.artemis.World;
import com.artemis.annotations.Wire;
import com.artemis.managers.GroupManager;

public class CustomGroupManager extends GroupManager {

    public void add(int entity, String group) {
        super.add(getWorld().getEntity(entity), group);
    }
}

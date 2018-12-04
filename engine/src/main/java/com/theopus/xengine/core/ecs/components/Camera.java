package com.theopus.xengine.core.ecs.components;

import com.artemis.Component;
import com.artemis.annotations.EntityId;

public class Camera extends Component {
    @EntityId
    public int target = -1;
    public float distance;
}

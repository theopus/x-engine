package com.theopus.client.events;

import com.theopus.xengine.event.Topic;

public abstract class CustomTopics {

    public static final int MOVE = 3;
    public static final Topic<Boolean> MOVE_TOPIC = new Topic<>(MOVE, Boolean.class);

    // right
    // left
    // forward
    // back
    // up
    // down

    // rot x
    // rot y
    // rot z
}

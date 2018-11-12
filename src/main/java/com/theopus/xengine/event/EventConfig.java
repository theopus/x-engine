package com.theopus.xengine.event;

public class EventConfig {
    private final int batchRetention;
    private final Topic<?>[] topics;

    public EventConfig(int batchRetention, Topic<?>... topics) {
        this.batchRetention = batchRetention;
        this.topics = topics;
    }

    public int getBatchRetention() {
        return batchRetention;
    }

    public Topic<?>[] getTopics() {
        return topics;
    }
}

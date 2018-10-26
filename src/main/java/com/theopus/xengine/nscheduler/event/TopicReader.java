package com.theopus.xengine.nscheduler.event;

import java.util.stream.Stream;

public class TopicReader<D> {

    private final int id;
    private final int topicId;
    private final EventManager manager;
    private Stream<Event<D>> events;

    public TopicReader(int id, int topicId, EventManager manager) {
        this.id = id;
        this.topicId = topicId;
        this.manager = manager;
    }

    public Stream<Event<D>> read() {
        return events;
    }

    /**
     *  Notify about read end
     */
    public void flush() {
        events = null;
    }

    /**
     * Get data for next read from manager
     */
    public void prepare() {
        events = this.manager.read(topicId, id);
    }
}

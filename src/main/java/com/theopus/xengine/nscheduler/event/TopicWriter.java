package com.theopus.xengine.nscheduler.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TopicWriter<D> {

    private final int topicId;
    private final EventManager eventManager;
    private List<Event<D>> events = new ArrayList<>();

    public TopicWriter(int topicId, EventManager eventManager) {
        this.topicId = topicId;
        this.eventManager = eventManager;
    }

    public void prepare() {
        events.clear();
    }

    public void write(Event<D> event) {
        events.add(event);
    }

    public void write(Collection<Event<D>> event) {
        events.addAll(event);
    }

    public void flush() {
        eventManager.put(topicId, events);
    }
}

package com.theopus.xengine.event;

import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TopicWriter<D> implements TaskComponent {

    private final int topicId;
    private final EventManager eventManager;
    private List<Event<D>> events = new ArrayList<>();

    public TopicWriter(int topicId, EventManager eventManager) {
        this.topicId = topicId;
        this.eventManager = eventManager;
    }

    public boolean prepare() {
        events.clear();
        return true;
    }

    @Override
    public boolean rollback() {
        return false;
    }

    public void write(Event<D> event) {
        events.add(event);
    }

    public void write(Collection<Event<D>> event) {
        events.addAll(event);
    }

    @Override
    public boolean finish() {
        eventManager.put(topicId, events);
        return true;
    }
}

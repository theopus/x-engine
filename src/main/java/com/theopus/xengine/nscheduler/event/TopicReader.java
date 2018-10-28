package com.theopus.xengine.nscheduler.event;

import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.util.stream.Stream;

public class TopicReader<D> implements TaskComponent {

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
     * Notify about read end
     */
    @Override
    public boolean finish() {
        events = null;
        return true;
    }

    /**
     * Get data for next read from manager
     */
    @Override
    public boolean prepare() {
        events = this.manager.read(topicId, id);
        return true;
    }

    @Override
    public boolean rollback() {
        return false;
    }
}

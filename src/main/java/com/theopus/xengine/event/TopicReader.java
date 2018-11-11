package com.theopus.xengine.event;

import com.theopus.xengine.nscheduler.task.TaskComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

public class TopicReader<D> implements TaskComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(TopicReader.class);

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

    public int offset() {
        return manager.getOffsetValue(id, topicId);
    }

    /**
     * Notify about read end
     */
    @Override
    public boolean finish() {
        LOGGER.debug("Finish...");
        events = null;
        return true;
    }

    /**
     * Get data for next read from manager
     */
    @Override
    public boolean prepare() {
        LOGGER.debug("Prepare...");
        events = this.manager.read(topicId, id);
        return true;
    }

    @Override
    public boolean rollback() {
        return false;
    }

    public int toRead() {
        return manager.toRead(id, topicId);
    }
}

package com.theopus.xengine.event;

import com.google.common.collect.ImmutableMap;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.Status;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.nscheduler.task.Task;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class EventManager {


    private static final AtomicInteger count = new AtomicInteger();
    private Scheduler scheduler;
    private Map<Integer, Topic<?>> map;
    private List<ListenEntry> entryList = new ArrayList<>();

    @Inject
    public EventManager(Scheduler scheduler) {
        this();
        this.scheduler = scheduler;
    }

    public EventManager() {
        this.map = ImmutableMap.of(
                EventManager.Topics.INPUT_DATA.getId(), EventManager.Topics.INPUT_DATA,
                EventManager.Topics.FRAMEBUFFER_CHANGED.getId(), EventManager.Topics.FRAMEBUFFER_CHANGED
        );
    }

    public <D> void put(int topicId, List<Event<D>> event) {
        Topic<D> top = (Topic<D>) map.get(topicId);
        top.put(event);
    }

    public int getOffsetValue(int reader, int topic) {
        Topic<?> t = map.get(topic);
        return t.getOffsetFor(reader);
    }

    public int toRead(int reader, int topic) {
        Topic<?> topic1 = map.get(topic);
        return topic1.toRead(reader);
    }

    public <D> Stream<Event<D>> read(int topicId, int userId) {
        Topic<D> top = (Topic<D>) map.get(topicId);
        return top.readAs(userId);
    }

    public <D> TopicReader<D> createReader(Topic<D> topic) {
        return new TopicReader<>(count.getAndIncrement(), topic.id, this);
    }

    public <D> TopicReader<D> createReader(int topicId) {
        return new TopicReader<>(count.getAndIncrement(), topicId, this);
    }

    public <D> TopicWriter<D> createWriter(Topic<D> topic) {
        return new TopicWriter<>(topic.id, this);
    }

    public <D> TopicWriter<D> createWriter(int topicId) {
        return new TopicWriter<>(topicId, this);
    }

    public void logTopics() {
        this.map.values().forEach(Topic::logTopic);
    }

    public void trimTo(int batch) {
        this.map.values().forEach(topic -> topic.trimTo(batch));
    }

    public EventProvider getProvider() {
        return new EventProvider(this);
    }

    public void listen(Task callback, TopicReader<Vector2i> reader) {
        entryList.add(new ListenEntry(callback, reader));
    }

    public ComponentTask task(int trimLast) {
        return new ComponentTask(Context.INLINE, true) {
            @Override
            public void process() throws Exception {
                trimTo(trimLast);
                for (ListenEntry listenEntry : entryList) {
                    TopicReader<?> reader = listenEntry.reader;
                    Task callback = listenEntry.task;
                    if (reader.toRead() != 0) {
                        if ((callback.getStatus() == Status.FINISHED) || (callback.getStatus() == Status.NEW)) {
                            callback.setStatus(Status.NEW);
                            scheduler.propose(callback);
                        }
                    }
                }
            }
        };
    }

    public static final class Topics {
        public static final Topic<InputData> INPUT_DATA = new Topic<>(InputData.class);
        public static final Topic<Vector2i> FRAMEBUFFER_CHANGED = new Topic<>(Vector2i.class);
    }

    private static final class ListenEntry {
        private Task task;
        private TopicReader<?> reader;

        private ListenEntry(Task callback, TopicReader<Vector2i> reader) {
            task = callback;
            this.reader = reader;
        }
    }
}

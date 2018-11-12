package com.theopus.xengine.event;

import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.Status;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.nscheduler.task.Task;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
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
    public EventManager(EventConfig config, Scheduler scheduler) {
        this(scheduler);
        Stream.of(config.getTopics()).forEach(this::addTopic);
    }


    public EventManager(Scheduler scheduler) {
        this();
        this.scheduler = scheduler;
    }

    public EventManager() {
        this.map = new HashMap<>();
        addTopic(Topics.INPUT_DATA_TOPIC);
        addTopic(Topics.FRAMEBUFFER_CHANGED_TOPIC);
    }

    private void addTopic(Topic<?> topic) {
        map.merge(topic.id, topic, (t1, t2) -> {
            throw new RuntimeException("Key " + topic.id + " is already registered as topic key.");
        });
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

    public void listen(Task callback, TopicReader<?> reader) {
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
        public static final int INPUT_DATA = 0;
        public static final Class INPUT_DATA_CLASS = InputData.class;
        public static final int FRAMEBUFFER_CHANGED = 1;
        public static final Class FRAMEBUFFER_CHANGED_CLASS = Vector2i.class;


        public static final Topic<InputData> INPUT_DATA_TOPIC = new Topic<>(INPUT_DATA, InputData.class);
        public static final Topic<Vector2i> FRAMEBUFFER_CHANGED_TOPIC = new Topic<>(FRAMEBUFFER_CHANGED, Vector2i.class);
    }

    private static final class ListenEntry {
        private Task task;
        private TopicReader<?> reader;

        private ListenEntry(Task callback, TopicReader<?> reader) {
            task = callback;
            this.reader = reader;
        }
    }
}

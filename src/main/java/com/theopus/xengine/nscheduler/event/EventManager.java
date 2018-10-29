package com.theopus.xengine.nscheduler.event;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.ComponentTask;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class EventManager {

    public static final class Topics {
        public static final Topic<InputData> INPUT_DATA_TOPIC = new Topic<>(InputData.class);
    }

    private static final AtomicInteger count = new AtomicInteger();
    private Map<Integer, Topic<?>> map;

    public EventManager(Map<Integer, Topic<?>> map) {
        this.map = map;
    }

    public <D> void put(int topicId, List<Event<D>> event) {
        Topic<D> top = (Topic<D>) map.get(topicId);
        top.put(event);
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


    public ComponentTask task(int trimLast){
        return new ComponentTask(Context.INLINE, true) {
            @Override
            public void process() throws Exception {
                trimTo(trimLast);
            }
        };
    }
}

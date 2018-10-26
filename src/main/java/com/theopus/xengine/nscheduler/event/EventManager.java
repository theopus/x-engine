package com.theopus.xengine.nscheduler.event;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

public class EventManager {

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

    private<D> TopicReader<D> createReader(Topic<D> topic){
        return new TopicReader<>(count.getAndIncrement(), topic.id, this);
    }

    private<D> TopicWriter<D> createWriter(Topic<D> topic){
        return new TopicWriter<>(topic.id, this);
    }

    private void logTopics(){
        this.map.values().forEach(Topic::logTopic);
    }

    private void trimTo(int batch){
        this.map.values().forEach(topic -> topic.trimTo(batch));
    }
}

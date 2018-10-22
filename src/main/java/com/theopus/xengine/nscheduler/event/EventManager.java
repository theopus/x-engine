package com.theopus.xengine.nscheduler.event;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

public class EventManager {
    private Map<Integer, Topic<?>> map;

    public EventManager(Map<Integer, Topic<?>> map) {
        this.map = map;
    }

    public <D> void put(int topicId, Queue<Event<D>> event) {
        Topic<D> top = (Topic<D>) map.get(topicId);
        top.put(event);
    }


    public <D> Stream<Event<D>> read(int topicId, int userId) {
        Topic<D> top = (Topic<D>) map.get(topicId);
        return top.readAs(userId);
    }

    private void logTopics(){
        this.map.values().forEach(Topic::logTopic);
    }

    private void trimTo(int batch){
        this.map.values().forEach(topic -> topic.trimTo(batch));
    }
}

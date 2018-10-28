package com.theopus.xengine.nscheduler.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

public class Topic<D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Topic.class);
    private static int count;

    int id;
    private String description;
    private Class<D> dataClass;

    private Map<Integer, Integer> userOffsets;


    private BlockingQueue<Event<D>> events = new LinkedBlockingQueue<>();
    private int nextBatchOffset = 0;
    private int bcount = 0;
    private Map<Integer, Integer> bathces = new LinkedHashMap<>();
    public Topic(String description, Class<D> dataClass) {
        this.id = count++;
        this.userOffsets = new HashMap<>();
        this.dataClass = dataClass;
        this.description = description;
    }

    public Topic(Class<D> dataClass) {
        this("Topic for: " + dataClass, dataClass);
    }

    void put(List<Event<D>> newEvents) {
        bathces.put(bcount++, nextBatchOffset);

        nextBatchOffset += newEvents.size();
        events.addAll(newEvents);
    }

    Stream<Event<D>> readAs(int userId) {
        int size = events.size();
        int offset = getOffsetFor(userId);

        if (offset > events.size()) {
            System.err.println("Error");
        }

        moveOffset(userId, size - offset);
        return events.stream().skip(offset);
    }

    private int getOffsetFor(int userId) {
        Integer offset = userOffsets.get(userId);
        if (offset == null || offset == 0) {
            userOffsets.put(userId, 0);
            return 0;
        } else {
            return offset;
        }
    }

    private void moveOffsets(int read) {
        moveOffsets(read, userOffsets);
    }

    private void moveBatchesOffsets(int read) {
        moveOffsets(read, bathces);
    }

    private void moveOffsets(int read, Map<Integer, Integer> bathces) {
        bathces.entrySet().forEach(integerIntegerEntry -> {
            int diff = integerIntegerEntry.getValue() + read;
            if (diff <= 0) {
                diff = 0;
            }
            bathces.put(integerIntegerEntry.getKey(), diff);
        });
    }

    int moveOffset(int userId, int read) {
        Integer offset = userOffsets.get(userId);
        userOffsets.put(userId, offset += read);
        return offset;
    }

    public void logTopic() {
        LOGGER.info("\nTopic queue :{}\nTopic users : {}\nTopic batches : {}", events, userOffsets, bathces);
    }

    public void trimTo(int toLast) {
        Collection<Object> objects = new ArrayList<>();
        if (toLast == 0) {
            events.drainTo(objects);
            events.clear();
        } else {
            int trimBuckets = bcount - toLast;
            if (trimBuckets <= 0){
                return;
            }
            events.drainTo(objects, bathces.get(trimBuckets));
            userOffsets.keySet().forEach(integer -> {
                if (integer < trimBuckets) {
                    bathces.remove(integer);
                }
            });
        }
        moveOffsets(-objects.size());
        moveBatchesOffsets(-objects.size());
        LOGGER.info("Trimmed {}", objects.size());
    }

    public int getId() {
        return id;
    }
}

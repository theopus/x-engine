package com.theopus.xengine.core.events;

import java.util.*;

public class EventBus {

    private Map<Class, List<Object>> events = new HashMap<>();
    private Map<Class, List<Subscriber>> subscriptions = new HashMap<>();

    public void subscribe(Class eventClass, Subscriber subscriber) {
        getSubscribersList(eventClass).add(subscriber);
    }

    public void post(Object event) {
        List<Object> eventList = this.events.get(event.getClass());
        if (Objects.isNull(eventList)) {
            eventList = new ArrayList<>();
            this.events.put(event.getClass(), eventList);
        }
        eventList.add(event);
    }

    public void propagate() {
        for (Map.Entry<Class, List<Object>> te : events.entrySet()) {
            Class eventClass = te.getKey();
            List<Object> eventsList = te.getValue();
            for (Subscriber subscriber : getSubscribersList(eventClass)) {
                for (Object o : eventsList) {
                    subscriber.onEvent(o);
                }
            }
            eventsList.clear();
        }
    }

    private List<Subscriber> getSubscribersList(Class eventClass) {
        List<Subscriber> subscribers = this.subscriptions.get(eventClass);
        if (Objects.isNull(subscribers)) {
            subscribers = new ArrayList<>();
            this.subscriptions.put(eventClass, subscribers);
        }
        return subscribers;
    }
}
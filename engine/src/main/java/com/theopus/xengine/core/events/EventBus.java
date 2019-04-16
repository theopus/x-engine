package com.theopus.xengine.core.events;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class EventBus {

    private Map<Class, List<Object>> events = new HashMap<>();
    private Map<Class, List<Subscriber>> subscriptions = new HashMap<>();

    public void subscribe(Class eventClass, Subscriber subscriber) {
        getSubscribersList(eventClass).add(subscriber);
    }

    public void subscribe(Object subscriber) {

        Type[] genericInterfaces = subscriber.getClass().getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            if (genericInterface instanceof ParameterizedType && ((ParameterizedType) genericInterface).getRawType().getTypeName().equals(Subscriber.class.getTypeName())) {
                Type[] genericTypes = ((ParameterizedType) genericInterface).getActualTypeArguments();

                for (Type genericType : genericTypes) {
                    Class<?> eventClass = (Class<?>) genericType;
                    getSubscribersList(eventClass).add((Subscriber) subscriber);
                }
            }
        }

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

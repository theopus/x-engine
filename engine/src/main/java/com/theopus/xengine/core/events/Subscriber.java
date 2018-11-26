package com.theopus.xengine.core.events;

public interface Subscriber<T> {
    void onEvent(T t);
}

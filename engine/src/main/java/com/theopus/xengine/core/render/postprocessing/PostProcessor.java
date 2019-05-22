package com.theopus.xengine.core.render.postprocessing;

public interface PostProcessor<T> {
    T doProcess(T t);
    void doProcessAsLast();
}

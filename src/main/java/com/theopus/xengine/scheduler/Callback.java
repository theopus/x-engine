package com.theopus.xengine.scheduler;

public interface Callback {
    Callback empty = () -> {};
    void call();


}

package com.theopus.xengine.nscheduler;

import java.util.concurrent.ExecutionException;

public interface Feeder {
    void feed(Task task);

    void drain() throws ExecutionException, InterruptedException;

    void close();
}

package com.theopus.xengine.nscheduler.lock;

public interface LockFactory<T> {

    Lock<T> create(int id);
}

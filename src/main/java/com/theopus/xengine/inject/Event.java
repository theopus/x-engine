package com.theopus.xengine.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Event {
    int READ = 0;
    int WRITE = 1;

    int topicId();

    int type();
}

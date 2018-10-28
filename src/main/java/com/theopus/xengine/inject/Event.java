package com.theopus.xengine.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inject
@Retention(RetentionPolicy.RUNTIME)
public @interface Event{
    int READ = 0;
    int WRITE = 2;

    int topicId();

    int type();
}

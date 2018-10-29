package com.theopus.xengine.inject;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Inject
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectEvent {
    int READ = 0;
    int WRITE = 1;

    int topicId();

    int type();
}

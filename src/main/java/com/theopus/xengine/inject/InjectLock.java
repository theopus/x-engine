package com.theopus.xengine.inject;

import com.theopus.xengine.nscheduler.lock.Lock;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.theopus.xengine.nscheduler.lock.Lock.Type.WRITE_READ;

@Inject
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectLock {
    Lock.Type value() default WRITE_READ;
}

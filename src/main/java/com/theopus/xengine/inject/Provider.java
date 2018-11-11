package com.theopus.xengine.inject;

import com.theopus.xengine.nscheduler.task.TaskComponent;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public interface Provider {
    void provide(Object target, List<TaskComponent> container) throws IllegalAccessException;

    default List<Field> collectFields(Class<?> tc) {
        List<Field> result = new ArrayList<>();
        for (Class cl = tc; cl != null; cl = cl.getSuperclass()) {
            result.addAll(Arrays.asList(cl.getDeclaredFields()));
        }
        return result;
    }
}

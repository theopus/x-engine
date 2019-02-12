package com.theopus.xengine.core.render;

import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;

import java.util.Map;
import java.util.Set;

/**
 * @param <T> is data-type for uploading
 */
public interface RenderModule<T> {
    String load(T t);

    void bind(String model, int entityId);

    void render();

    Set<String> models();

    String getPrefix();

    void prepare();

    void finish();

    Map<String, ImmutableBag<Entity>> bindings();

}

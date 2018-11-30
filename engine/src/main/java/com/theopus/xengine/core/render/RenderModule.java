package com.theopus.xengine.core.render;

import com.artemis.Component;

/**
 * @param <T> is data-type for uploading
 */
public interface RenderModule<T> {
    String load(T t);

    void bind(String model, int entityId);

    void render();

    void prepare();

    void finish();
}

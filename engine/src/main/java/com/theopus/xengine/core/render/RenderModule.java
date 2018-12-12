package com.theopus.xengine.core.render;

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

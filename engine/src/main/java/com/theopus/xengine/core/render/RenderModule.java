package com.theopus.xengine.core.render;

import com.artemis.Component;
import org.joml.Matrix4f;

/**
 * @param <T> is data-type for uploading
 */
public interface RenderModule<T> {
    String load(T t);

    void bind(String model, int entityId);

    void render();

    void prepare();

    void finish();

    void loadViewMatrix(Matrix4f view);
}

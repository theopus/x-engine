package com.theopus.xengine.core.render;

import com.artemis.Entity;
import com.artemis.utils.ImmutableBag;

import java.util.Map;
import java.util.Set;

/**
 * @param <T> is data-type for uploading
 */
public interface RenderModule<T> {

    public void  init();

    String loadToModule(T t);
    String loadToModule(String title, T t);

    void bind(String model, int entityId);

    void renderModule();

    Set<String> models();

    String getPrefix();

    void prepareModule();

    void finishModule();

    Map<String, ImmutableBag<Entity>> bindings();

}

package com.theopus.xengine.render;

import com.theopus.xengine.trait.EntityManager;

import java.util.BitSet;

public interface RenderModule<T> {
    void bind(int entityId, int entryId);

    void prepare(int entryId);

    void finish(int entryId);

    void render(T trait);

    void render(BitSet entities, EntityManager em);

    void close();
}

package com.theopus.xengine.render;

import com.theopus.xengine.ecs.mapper.ViewEntityManager;

import java.util.BitSet;

public interface RenderModule {
    void bind(int entityId, int entryId);

    void prepare(int entryId);

    void finish(int entryId);

    void render(BitSet entities);

    void close();

    void render(BitSet entities, ViewEntityManager em);
}

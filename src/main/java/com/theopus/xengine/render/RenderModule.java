package com.theopus.xengine.render;

import com.theopus.xengine.ecs.mapper.ViewEntityManager;
import org.joml.Matrix4f;

import java.util.BitSet;

public interface RenderModule {
    void bind(int entityId, int entryId);

    void prepare(int entryId);

    void finish(int entryId);

    void render(BitSet entities);

    void close();

    void render(BitSet entities, ViewEntityManager em);

    void loadView(Matrix4f view);

    void loadProjection(Matrix4f projection);
}

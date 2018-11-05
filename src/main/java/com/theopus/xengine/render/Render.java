package com.theopus.xengine.render;

import com.theopus.xengine.trait.EntityManager;
import org.joml.Matrix4f;

import java.io.Closeable;
import java.util.BitSet;

public interface Render extends Closeable {

    void init();

    void loadProjection(Matrix4f projection);

    void loadViewPort(int w, int h);

    void loadView(Matrix4f view);

    /**
     * assumes that render aware of render trait, and has it inside
     *
     * @param entity
     */
    void render(int entity);

    void render(BitSet entities);

    @Deprecated
    void render(int model, Matrix4f transform);

    void clean();

    void bind(int entityId, Class<? extends RenderModule> module, int entryId);

    void prepare(EntityManager em);

    @SuppressWarnings("unchecked")
    <T extends RenderModule> T module(Class<T> module);
}

package com.theopus.xengine.render.opengl;

import com.theopus.xengine.render.Render;
import com.theopus.xengine.render.RenderModule;
import com.theopus.xengine.trait.EntityManager;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;
import java.util.LinkedHashMap;
import java.util.Map;


public class GlRender implements Render {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlRender.class);


    private Map<Class<? extends RenderModule>, ? extends RenderModule> modules;
    private EntityManager em;

    public GlRender(Map<Class<? extends RenderModule>, ? extends RenderModule> modules) {
        this.modules = new LinkedHashMap<>(modules);
    }

    @Override
    public void init() {

    }

    @Override
    public void loadProjection(Matrix4f projection) {
        LOGGER.info("Projections reload \n{}", projection);
    }

    @Override
    public void loadViewPort(int w, int h) {
        GL11.glViewport(0, 0, w, h);
    }

    @Override
    public void loadView(Matrix4f view) {

    }

    @Override
    public void render(int entity) {
        BitSet entitys = new BitSet();
        entitys.set(entity);
        render(entitys);
    }

    @Override
    public void render(BitSet entities) {
        for (RenderModule value : modules.values()) {
            value.render(entities, em);
        }
    }

    @Override
    public void render(int model, Matrix4f transform) {

    }

    @Override
    public void clean() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void bind(int entityId, Class<? extends RenderModule> module, int entryId) {
        RenderModule renderModule = modules.get(module);
        renderModule.bind(entityId, entryId);
    }

    @Override
    public void prepare(EntityManager em) {
        this.em = em;
    }

    public void close() {
        modules.values().forEach(RenderModule::close);
    }


    @SuppressWarnings("unchecked")
    @Override
    public <T extends RenderModule> T module(Class<T> module) {
        return (T) modules.get(module);
    }
}
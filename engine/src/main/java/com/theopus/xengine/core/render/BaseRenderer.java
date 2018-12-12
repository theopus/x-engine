package com.theopus.xengine.core.render;

import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRenderer {

    protected List<RenderModule> modules;

    public BaseRenderer() {
        this.modules = new ArrayList<>();
    }

    public void render() {
        for (RenderModule module : modules) {
            module.prepare();
            module.render();
            module.finish();
        }
    }

    public void add(RenderModule<?> module) {
        if (!modules.contains(module)) {
            modules.add(module);
        }
    }

    /*
    slow temporary shit
     */
    public <T extends RenderModule<?>> T get(Class<T> moduleClass) {
        for (RenderModule module : modules) {
            if (module.getClass().equals(moduleClass)) {
                return (T) module;
            }
        }
        throw new RuntimeException(String.format("Module %s not found", moduleClass));
    }

    public abstract void clearBuffer();

    public abstract void loadProjectionMatrix(Matrix4f projection);

    public abstract void loadViewMatrix(Matrix4f view);

    public abstract void loadFramebufferSize(int width, int height);
}

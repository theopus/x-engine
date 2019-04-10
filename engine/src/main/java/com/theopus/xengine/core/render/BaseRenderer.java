package com.theopus.xengine.core.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRenderer {

    protected List<RenderModule<?>> modules;
    protected Map<Class<RenderModule<?>>, RenderModule<?>> renderModuleMap;

    public Map<Class<RenderModule<?>>, RenderModule<?>> moduleMap() {
        return renderModuleMap;
    }

    public BaseRenderer() {
        this.modules = new ArrayList<>();
        this.renderModuleMap = new HashMap<>();
    }

    public void render() {
        for (RenderModule module : modules) {
            module.prepareModule();
            module.renderModule();
            module.finishModule();
        }
    }

    public void add(Class<RenderModule<?>> clazz, RenderModule<?> module) {
        if (!modules.contains(module)) {
            modules.add(module);
        }
        renderModuleMap.put(clazz, module);
    }

    public <T extends RenderModule<?>> T get(Class<T> moduleClass) {
        return (T) renderModuleMap.get(moduleClass);
    }
    /*
    slow temporary shit
     */
    public <T extends RenderModule<?>> T getDirect(Class<T> moduleClass) {
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

    public List<RenderModule<?>> modules() {
        return modules;
    }



    public abstract void loadLight(Vector3f diffuse, Vector3f position);
}

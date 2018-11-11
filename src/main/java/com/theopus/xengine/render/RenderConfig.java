package com.theopus.xengine.render;

import java.util.Map;

public class RenderConfig {
    private final Map<Class<? extends RenderModule>, Class<? extends RenderModule>> modules;
    private final Class<? extends Render> render;

    public RenderConfig(Class<? extends Render> render, Map<Class<? extends RenderModule>, Class<? extends RenderModule>> modules) {
        this.render = render;
        this.modules = modules;
    }

    public Map<Class<? extends RenderModule>, Class<? extends RenderModule>> getModules() {
        return modules;
    }

    public Class<? extends Render> getRender() {
        return render;
    }
}

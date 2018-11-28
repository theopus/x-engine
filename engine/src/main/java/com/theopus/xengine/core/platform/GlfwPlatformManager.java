package com.theopus.xengine.core.platform;

import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.events.VoidEvent;
import com.theopus.xengine.wrapper.glfw.GlfwWrapper;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.lwjgl.glfw.GLFW;

public class GlfwPlatformManager implements PlatformManager {

    private final EventBus eventBus;
    private GlfwWrapper wrapper;

    public GlfwPlatformManager(WindowConfig config, EventBus eventBus) {
        this.wrapper = new GlfwWrapper(config);
        this.eventBus = eventBus;


    }

    @Override
    public void init(){
        wrapper.setKeyCallback((window, key, scancode, action, mods) -> {
            if (action != GLFW.GLFW_REPEAT) {
//                wrapper.setShouldClose(true);
                eventBus.post(new VoidEvent());
            }
        });
    }

    @Override
    public void createWindow() {
        wrapper.createWindow();
        wrapper.showWindow();
    }

    public void processEvents() {
        wrapper.processEvents();
    }

    @Override
    public void refreshWindow() {
        wrapper.refreshWindow();
    }

    @Override
    public void close() {
        wrapper.close();
    }

    @Override
    public boolean shouldClose() {
        return wrapper.shouldClose();
    }

    @Override
    public void clearColorBuffer() {
        wrapper.clearColorBuffer();
    }
}

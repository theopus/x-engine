package com.theopus.xengine.core.platform;

import com.google.common.collect.ImmutableMap;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.events.VoidEvent;
import com.theopus.xengine.core.input.InputAction;
import com.theopus.xengine.core.input.InputActionType;
import com.theopus.xengine.core.input.InputEvent;
import com.theopus.xengine.wrapper.glfw.GlfwWrapper;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.lwjgl.glfw.GLFW;

import java.util.Map;

public class GlfwPlatformManager implements PlatformManager {

    private final EventBus eventBus;
    private GlfwWrapper wrapper;

    private Map<Integer, InputAction> keymap = ImmutableMap.of(
            GLFW.GLFW_KEY_D, InputAction.RIGHT,
            GLFW.GLFW_KEY_A, InputAction.LEFT,
            GLFW.GLFW_KEY_W, InputAction.UP,
            GLFW.GLFW_KEY_S, InputAction.DOWN
    );

    public GlfwPlatformManager(WindowConfig config, EventBus eventBus) {
        this.wrapper = new GlfwWrapper(config);
        this.eventBus = eventBus;
    }

    @Override
    public void init(){
        wrapper.setKeyCallback((window, key, scancode, action, mods) -> {
            if (action != GLFW.GLFW_REPEAT) {
                eventBus.post(new InputEvent(keymap.getOrDefault(key, InputAction.UNIDENTIFIED), action == GLFW.GLFW_PRESS ? InputActionType.BEGIN : InputActionType.END));
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

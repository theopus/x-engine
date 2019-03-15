package com.theopus.xengine.core.platform;

import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.input.InputAction;
import com.theopus.xengine.core.input.InputActionType;
import com.theopus.xengine.core.input.InputEvent;
import com.theopus.xengine.wrapper.glfw.GlfwWrapper;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class GlfwPlatformManager implements PlatformManager {

    @Wire
    private EventBus eventBus;
    private GlfwWrapper wrapper;

    private Map<Integer, InputAction> keymap = new HashMap<Integer, InputAction>() {{
        put(GLFW.GLFW_KEY_D, InputAction.RIGHT);
        put(GLFW.GLFW_KEY_A, InputAction.LEFT);
        put(GLFW.GLFW_KEY_SPACE, InputAction.UP);
        put(GLFW.GLFW_KEY_C, InputAction.DOWN);
        put(GLFW.GLFW_KEY_W, InputAction.FORWARD);
        put(GLFW.GLFW_KEY_S, InputAction.BACK);
        put(GLFW.GLFW_KEY_E, InputAction.ROTATE_CW);
        put(GLFW.GLFW_KEY_Q, InputAction.ROTATE_CCW);
    }};


    public GlfwPlatformManager(WindowConfig config) {
        this.wrapper = new GlfwWrapper(config);
    }

    public GlfwPlatformManager(WindowConfig config, EventBus eventBus) {
        this.wrapper = new GlfwWrapper(config);
        this.eventBus = eventBus;
    }

    @Override
    public void init() {
        createWindow();
        wrapper.setKeyCallback((window, key, scancode, action, mods) -> {
            if (action != GLFW.GLFW_REPEAT) {
                eventBus.post(new InputEvent(keymap.getOrDefault(key, InputAction.UNIDENTIFIED), action == GLFW.GLFW_PRESS ? InputActionType.BEGIN : InputActionType.END));
            }
        });
        wrapper.setFramebufferChangedCallback((window, width, height) -> eventBus.post(new FramebufferEvent(width, height)));
        eventBus.post(new FramebufferEvent(wrapper.getWidth(), wrapper.getHeight()));
        eventBus.post(new FramebufferEvent(wrapper.getWidth(), wrapper.getHeight()));
        eventBus.post(new FramebufferEvent(wrapper.getWidth(), wrapper.getHeight()));
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

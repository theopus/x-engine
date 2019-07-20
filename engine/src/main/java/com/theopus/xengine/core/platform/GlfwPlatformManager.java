package com.theopus.xengine.core.platform;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;

import com.artemis.annotations.Wire;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.input.InputAction;
import com.theopus.xengine.core.input.InputActionType;
import com.theopus.xengine.core.input.InputCursorEvent;
import com.theopus.xengine.core.input.InputEvent;
import com.theopus.xengine.wrapper.glfw.GlfwWrapper;
import com.theopus.xengine.wrapper.glfw.WindowConfig;

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
        put(GLFW.GLFW_MOUSE_BUTTON_LEFT, InputAction.ACTION0);
        put(GLFW.GLFW_MOUSE_BUTTON_RIGHT, InputAction.ACTION1);
    }};

    private Map<InputAction, InputActionType> state = new HashMap<>(keymap.values()
            .stream()
            .collect(Collectors.toMap(Function.identity(),
                    o -> InputActionType.END)));


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
                handleKeyPress(key, action);
            }
        });
        wrapper.setMouseButtonCallback((window, key, action, mods) -> {
            if (action != GLFW.GLFW_REPEAT) {
                handleKeyPress(key, action);
            }
        });

        wrapper.setCursorPosCallback(new GLFWCursorPosCallback() {
            private double x = 0.0;
            private double y = 0.0;

            private double dx = 0.0;
            private double dy = 0.0;

            @Override
            public void invoke(long window, double xpos, double ypos) {
                dx = xpos - x;
                dy = ypos - y;

                x = xpos;
                y = ypos;
                eventBus.post(new InputCursorEvent(dx, dy, state));
            }
        });
        wrapper.setFramebufferChangedCallback((window, width, height) -> eventBus.post(new FramebufferEvent(width, height)));
        eventBus.post(new FramebufferEvent(wrapper.getWidth(), wrapper.getHeight()));
        eventBus.post(new FramebufferEvent(wrapper.getWidth(), wrapper.getHeight()));
        eventBus.post(new FramebufferEvent(wrapper.getWidth(), wrapper.getHeight()));
    }

    private void handleKeyPress(int key, int action) {
        InputAction a = keymap.getOrDefault(key, InputAction.UNIDENTIFIED);
        InputActionType t = action == GLFW.GLFW_PRESS ? InputActionType.BEGIN : InputActionType.END;
        eventBus.post(new InputEvent(a, t));
        state.put(a, t);
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
}

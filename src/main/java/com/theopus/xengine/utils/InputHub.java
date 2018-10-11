package com.theopus.xengine.utils;

import org.lwjgl.glfw.*;

import java.util.HashMap;
import java.util.Map;

public class InputHub {

    private final MouseKeyListener mouseKeyListener;
    private final KeyListener keyListener;
    private final MouseMoveListener mouseMoveListener;
    private final MouseScrollListener mouseScrollListener;


    public GLFWKeyCallbackI keyCallback() {
        return GLFWKeyCallback.create(keyListener);
    }

    public GLFWMouseButtonCallbackI mouseButtonCallback() {
        return GLFWMouseButtonCallback.create(mouseKeyListener);
    }

    public GLFWCursorPosCallbackI mousePosCallback() {
        return GLFWCursorPosCallback.create(mouseMoveListener);
    }

    public GLFWScrollCallback scrollCallback() {
        return GLFWScrollCallback.create(mouseScrollListener);
    }

    public InputHub() {
        this.keyListener = new KeyListener();
        this.mouseKeyListener = new MouseKeyListener();
        this.mouseMoveListener = new MouseMoveListener();
        this.mouseScrollListener = new MouseScrollListener();
    }

    public boolean isKeyPressed(int key) {
        return this.keyListener.isKeyPressed(key);
    }

    public boolean isMouseKeyPressed(int key) {
        return this.mouseKeyListener.isKeyPressed(key);
    }

    public double xAxisDelta(){
        return this.mouseMoveListener.xAxisDelta();
    }

    public double yAxisDelta(){
        return this.mouseMoveListener.yAxisDelta();
    }

    public float scrollDy(){
        return this.mouseScrollListener.scrollDy();
    }

    public void resetCursorDelta(){
        this.mouseMoveListener.reset();
    }


    public class KeyListener implements GLFWKeyCallbackI {
        private Map<Integer, Boolean> keyMap;

        private KeyListener() {
            this.keyMap = new HashMap<>();
        }

        @Override
        public void invoke(long window, int key, int scancode, int action, int mods) {
            keyMap.put(key, action != GLFW.GLFW_RELEASE);
        }

        private boolean isKeyPressed(int key) {
            return keyMap.getOrDefault(key, false);
        }
    }

    public class MouseKeyListener implements GLFWMouseButtonCallbackI {

        private final HashMap<Integer, Boolean> keyMap;

        private MouseKeyListener() {
            this.keyMap = new HashMap<>();
        }

        @Override
        public void invoke(long window, int button, int action, int mods) {
            keyMap.put(button, action != GLFW.GLFW_RELEASE);
        }

        private boolean isKeyPressed(int key) {
            return keyMap.getOrDefault(key, false);
        }
    }

    public class MouseScrollListener implements GLFWScrollCallbackI{

        private float dy;

        @Override
        public void invoke(long window, double xoffset, double yoffset) {
            this.dy = (float) yoffset;
        }

        public float scrollDy() {
            return dy;
        }
    }

    public class MouseMoveListener implements GLFWCursorPosCallbackI {

        private double x = 0.0;
        private double y = 0.0;

        private double dx = 0.0;
        private double dy = 0.0;

        private MouseMoveListener() {
        }

        @Override
        public void invoke(long window, double xpos, double ypos) {
            dx = xpos - x;
            dy = ypos - y;

            x = xpos;
            y = ypos;
        }

        public void reset(){
            dx = 0;
            dy = 0;
        }

        private double yAxisDelta() {
            return dy;
        }

        private double xAxisDelta() {
            return dx;
        }
    }
}

package com.theopus.xengine.wrapper.glfw;

import org.joml.Vector4f;

public class WindowConfig {

    private int width;
    private int height;
    private Vector4f color;
    private boolean primitivesCompatible;
    private int vSync;

    public WindowConfig(int width, int height, Vector4f color, int vSync) {
        this.width = width;
        this.height = height;
        this.color = color;
        this.vSync = vSync;
    }

    public WindowConfig(int width, int height, Vector4f color, boolean primitivesCompatible, int vSync) {
        this(width, height, color, vSync);
        this.primitivesCompatible = primitivesCompatible;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public boolean isPrimitivesCompatible() {
        return primitivesCompatible;
    }

    public int getvSync() {
        return vSync;
    }

    public void setvSync(int vSync) {
        this.vSync = vSync;
    }
}

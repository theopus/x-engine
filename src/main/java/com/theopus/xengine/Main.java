package com.theopus.xengine;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

public class Main {
    public static void main(String[] args) {
        WindowManager wm = new WindowManager(
          new WindowConfig(600,400, new Vector4f(0.8f,0.8f,0.8f,1), false, 0),
                (window, key, scancode, action, mods) -> {}

        );

        wm.createWindow();
        wm.showWindow();

        while (!wm.windowShouldClose()){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            wm.update();
        }
    }
}

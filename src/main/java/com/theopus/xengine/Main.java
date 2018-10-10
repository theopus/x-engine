package com.theopus.xengine;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws InterruptedException, IOException {

        WindowManager wm = new WindowManager(
                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
                (window, key, scancode, action, mods) -> {
                }

        );
//
//
        wm.createWindow();
        wm.showWindow();

        EntityLoader entityLoader = new EntityLoader();

        Entity entity = entityLoader.loadEntity(
                new float[]{
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,

                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,

                }, 6
        );


        StaticShader staticShader = new StaticShader("static.vert", "static.frag");
        Render render = new Render(staticShader);


        while (!wm.windowShouldClose()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            render.render(entity);
            wm.update();
            wm.swapBuffers();
        }


    }
}

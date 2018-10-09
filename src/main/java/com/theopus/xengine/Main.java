package com.theopus.xengine;

import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.remotery.Remotery;
import org.lwjgl.util.remotery.RemoteryGL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws InterruptedException {

        WindowManager wm = new WindowManager(
                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
                (window, key, scancode, action, mods) -> {
                }

        );
//
//

        wm.createWindow();
        wm.showWindow();
        wm.deatachContext();


        Scheduler scheduler = new Scheduler();


        CallbackTask<Object> renderingTask = CallbackTask.of(() -> {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            wm.swapBuffers();
            LOGGER.info("Rendering Task");
            Thread.sleep(1);
            return null;
        });

        renderingTask.callback(() -> scheduler.scheduleTask(renderingTask));

        CallbackTask<Object> mainCtxInit = CallbackTask.of(() -> {
            wm.attachMainContext();
            LOGGER.info("Inited Context");
            return null;
        }).callback(() -> scheduler.scheduleTask(renderingTask));


        scheduler.scheduleTask(mainCtxInit);


        while (!wm.windowShouldClose()) {
            wm.update();
        }


        scheduler.close();
    }
}

package com.theopus.xengine.core;

import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.injection.ArtemisFieldResolver;
import com.artemis.injection.FieldResolver;
import com.artemis.utils.reflect.Field;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.ecs.systems.EventSystem;
import com.theopus.xengine.core.ecs.systems.ModelMatrixSystem;
import com.theopus.xengine.core.ecs.systems.MoveSystem;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.wrapper.glfw.Context;
import com.theopus.xengine.wrapper.glfw.GlfwWrapper;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class XEngine {

    public void run() {


        GlfwWrapper wrapper = new GlfwWrapper(new WindowConfig(600, 400, new Vector4f(1, 0, 0, 0), false, 0), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key == GLFW.GLFW_KEY_ESCAPE) GLFW.glfwSetWindowShouldClose(window, true);
            }
        });

        wrapper.createWindow();
        wrapper.showWindow();
        wrapper.attachContext(Context.MAIN);

        EventBus eventBus = new EventBus();

        World world = new World(new WorldConfigurationBuilder()
                .with(new EventSystem())
                .with(new MoveSystem())
                .with(new ModelMatrixSystem())
                .build()
                .register(eventBus));

        int i = world.create();

        world.getMapper(ModelMatrix.class).create(i);
        world.getMapper(Velocity.class).create(i);
        world.getMapper(Position.class).create(i);

        long before = System.currentTimeMillis();
        long now;
        long elapsed;

        while (!wrapper.shouldClose()) {
            now = System.currentTimeMillis();
            elapsed = now - before;
            before = now;

            wrapper.clearColorBuffer();
            world.setDelta(elapsed);
            world.process();
            wrapper.processEvents();
            wrapper.refreshWindow();
        }

        wrapper.close();
    }
}

package com.theopus.xengine.core;

import com.artemis.Archetype;
import com.artemis.ArchetypeBuilder;
import com.artemis.World;
import com.artemis.WorldConfigurationBuilder;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Render;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.ecs.systems.EventSystem;
import com.theopus.xengine.core.ecs.systems.ModelMatrixSystem;
import com.theopus.xengine.core.ecs.systems.MoveSystem;
import com.theopus.xengine.core.ecs.systems.RenderSystem;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.platform.GlfwPlatformManager;
import com.theopus.xengine.core.platform.PlatformManager;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.render.GlRenderer;
import com.theopus.xengine.core.render.RenderModule;
import com.theopus.xengine.core.render.modules.Ver0Data;
import com.theopus.xengine.core.render.modules.Ver0Module;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.joml.Vector4f;

public class XEngine {

    public void run() {

        EventBus eventBus = new EventBus();

        PlatformManager platformManager = new GlfwPlatformManager(new WindowConfig(600, 400, new Vector4f(1, 0, 0, 0), false, 0), eventBus);
        platformManager.createWindow();

        platformManager.init();
        EventSystem eventSystem = new EventSystem();
        RenderSystem renderSystem = new RenderSystem();
        BaseRenderer render = new GlRenderer();

        World world = new World(new WorldConfigurationBuilder()
                .with(eventSystem)
                .with(new MoveSystem())
                .with(new ModelMatrixSystem())
                .with(renderSystem)
                .with(new CustomGroupManager())
                .build()
                .register(eventBus)
                .register(platformManager)
                .register("renderer", render)
        );

        RenderModule<Ver0Data> module = new Ver0Module();
        world.inject(module);

        String model0 = module.load(new Ver0Data(
                new float[]{
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,
                },
                new int[]{
                        0, 1, 3,
                        3, 1, 2
                }));


        Archetype base = new ArchetypeBuilder().add(ModelMatrix.class, Velocity.class, Position.class, Render.class).build(world);

        for (int j = 0; j < 50_000; j++) {
            int i1 = world.create(base);
            module.bind(model0, i1);
            render.add(module);
        }


        long before = System.currentTimeMillis();
        long now;
        long elapsed;

        while (!platformManager.shouldClose()) {
            now = System.currentTimeMillis();
            elapsed = now - before;
            before = now;

            platformManager.clearColorBuffer();
            world.setDelta(elapsed);
            world.process();

            platformManager.processEvents();
            platformManager.refreshWindow();
        }

        platformManager.close();
    }
}

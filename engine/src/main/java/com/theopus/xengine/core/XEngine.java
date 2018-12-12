package com.theopus.xengine.core;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.components.*;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.ecs.systems.*;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.input.InputEvent;
import com.theopus.xengine.core.platform.FramebufferEvent;
import com.theopus.xengine.core.platform.GlfwPlatformManager;
import com.theopus.xengine.core.platform.PlatformManager;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.core.render.GlRenderer;
import com.theopus.xengine.core.render.RenderModule;
import com.theopus.xengine.core.render.modules.v0.Ver0Data;
import com.theopus.xengine.core.render.modules.v0.Ver0Module;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.joml.Vector4f;

import java.util.concurrent.ThreadLocalRandom;

public class XEngine {

    public static final String MAIN_CAMERA = "MAIN_CAMERA";

    public void run() {
        EventBus eventBus = new EventBus();

        PlatformManager platformManager = new GlfwPlatformManager(new WindowConfig(600, 400, new Vector4f(1, 0, 0, 0), false, 0), eventBus);
        platformManager.createWindow();
        platformManager.init();

        GLContext glContext = new GLContext();
        BaseRenderer render = new GlRenderer(glContext);

        TagManager tagManager = new TagManager();

        EventSystem eventSystem = new EventSystem();
        RenderSystem renderSystem = new RenderSystem();
        CustomGroupManager groupManager = new CustomGroupManager();
        CameraSystem cameraSystem = new CameraSystem();
        ProjectionSystem projectionSystem = new ProjectionSystem();

        eventBus.subscribe(InputEvent.class, cameraSystem);
        eventBus.subscribe(FramebufferEvent.class, projectionSystem);
        World world = new World(new WorldConfigurationBuilder()
                .with(eventSystem)
                .with(new MoveSystem())
                .with(new ModelMatrixSystem())
                .with(cameraSystem)
                .with(projectionSystem)
                .with(renderSystem)
                .with(groupManager)
                .with(tagManager)
                .build()
                .register(eventBus)
                .register(platformManager)
                .register("renderer", render)
        );

        RenderModule<Ver0Data> module = new Ver0Module(glContext);
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

        for (int j = 0; j < 2; j++) {
            int i1 = world.create(base);
            module.bind(model0, i1);
            render.add(module);
            ComponentMapper<Position> mapper = world.getMapper(Position.class);
            Position position = mapper.get(i1);

            position.position.x = 0;
            position.position.y = 0;
            position.position.z = -1f;
        }

        int cameraAcnhor = world.create();
        world.getMapper(Position.class).create(cameraAcnhor);
        world.getMapper(Velocity.class).create(cameraAcnhor);
        Archetype cameraArchetype = new ArchetypeBuilder().add(Camera.class).build(world);

        int camera = world.create(cameraArchetype);
        Camera camera1 = world.getMapper(Camera.class).get(camera);
        camera1.target = cameraAcnhor;
        tagManager.register(MAIN_CAMERA, camera);

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
        glContext.close();
    }
}

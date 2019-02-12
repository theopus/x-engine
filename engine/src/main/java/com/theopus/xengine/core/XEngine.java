package com.theopus.xengine.core;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.ecs.systems.*;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.platform.GlfwPlatformManager;
import com.theopus.xengine.core.platform.PlatformManager;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.core.render.GlRenderer;
import com.theopus.xengine.core.render.modules.v0.Ver0Module;
import com.theopus.xengine.core.render.modules.v1.Ver1Module;
import com.theopus.xengine.core.render.modules.v2.Ver2Data;
import com.theopus.xengine.core.render.modules.v2.Ver2Module;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;
import com.theopus.xengine.core.utils.Reflection;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class XEngine {

    public static final String MAIN_CAMERA = "MAIN_CAMERA";

    private final List<Closeable> closeables = new ArrayList<>();

    public void run() throws IOException {
        List<Class<? extends ArtemisRenderModule<?, ?>>> modules = Arrays.asList(
                Ver0Module.class,
                Ver1Module.class,
                Ver2Module.class,
                Ver3Module.class
        );


        List<Class<? extends BaseSystem>> classes = Arrays.asList(
                TagManager.class,
                EventSystem.class,
                RenderSystem.class,
                CustomGroupManager.class,
                CameraSystem.class,
                ProjectionSystem.class,
                MoveSystem.class,
                ModelMatrixSystem.class,
                LightSystem.class
        );

        EventBus eventBus = new EventBus();
        PlatformManager platformManager = new GlfwPlatformManager(
                new WindowConfig(600, 400,
                        new Vector4f(128f / 256f, 128f / 256f, 128f / 256f, 0), false,
                        0),
                eventBus);
        platformManager.createWindow();
        platformManager.init();

        EntityFactory factory = new EntityFactory();

        BaseRenderer render = glRenderer(modules);
        List<BaseSystem> systems = createSystems(classes);

        WorldConfigurationBuilder configurationBuilder = new WorldConfigurationBuilder();
        systems.forEach(eventBus::subscribe);
        systems.forEach(configurationBuilder::with);
        WorldConfiguration config = configurationBuilder.build()
                .register(eventBus)
                .register(platformManager)
                .register("renderer", render)
                .register(factory);

        World world = new World(config);

        Ver0Module module0 = render.get(Ver0Module.class);
        Ver1Module module1 = render.get(Ver1Module.class);
        Ver2Module module2 = render.get(Ver2Module.class);

        //TODO: MOVE ASSETS LOADING SOMEWHERE
        //bullshiting
        String model0 = ModelUtils.simpleQuad(module0);
        String texmodel = ModelUtils.texturedQuad(module1);
        String objectModel = module2.load(new Ver2Data("objects/dragon.obj"));

        //-----------[

        render.inject(world);
        factory.inject(world);

        factory.createCamera();
        factory.createEntity(new Vector3f(0, 0, -5));
        factory.createLight(new Vector3f(1, 1, 1), new Vector3f(5, 1, 10f));

        new Loop.Builder()
                .setCondition(() -> !platformManager.shouldClose())
                .setRun(elapsed -> {
                    platformManager.clearColorBuffer();
                    world.setDelta(elapsed);
                    world.process();

                    platformManager.processEvents();
                    platformManager.refreshWindow();
                })
                .setOnClose(() -> {
                    platformManager.close();
                    for (Closeable closeable : closeables) {
                        closeable.close();
                    }
                })
                .createLoop()
                .run();
    }

    private BaseRenderer glRenderer(List<Class<? extends ArtemisRenderModule<?, ?>>> modules) {
        GLContext glContext = new GLContext();
        BaseRenderer render = new GlRenderer(glContext);

        for (Class<? extends ArtemisRenderModule<?, ?>> m : modules) {
            ArtemisRenderModule module = Reflection.newInstance(m);
            render.add(module);
            module.setContext(glContext);
        }


        closeables.add(glContext);
        return render;
    }

    private List<BaseSystem> createSystems(List<Class<? extends BaseSystem>> systems){
        return systems.stream().map(Reflection::newInstance).collect(Collectors.toList());
    }

    private Position createCamera(TagManager tagManager, World world) {
        int cameraAcnhor = world.create();
        world.getMapper(Position.class).create(cameraAcnhor);
        world.getMapper(Velocity.class).create(cameraAcnhor);
        Archetype cameraArchetype = new ArchetypeBuilder().add(Camera.class).build(world);

        int id = world.create(cameraArchetype);
        Camera camera = world.getMapper(Camera.class).get(id);
        camera.target = cameraAcnhor;
        tagManager.register(MAIN_CAMERA, id);
        return world.getMapper(Position.class).get(cameraAcnhor);
    }
}

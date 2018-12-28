package com.theopus.xengine.core;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.Archetypes;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;
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
import com.theopus.xengine.core.render.modules.v1.Ver1Data;
import com.theopus.xengine.core.render.modules.v1.Ver1Module;
import com.theopus.xengine.core.render.modules.v2.Ver2Data;
import com.theopus.xengine.core.render.modules.v2.Ver2Module;
import com.theopus.xengine.wrapper.glfw.WindowConfig;
import org.joml.Vector3f;
import org.joml.Vector4f;

public class XEngine {

    public static final String MAIN_CAMERA = "MAIN_CAMERA";

    public void run() {
        EventBus eventBus = new EventBus();

        PlatformManager platformManager = new GlfwPlatformManager(new WindowConfig(600, 400, new Vector4f(226f / 256f, 256f / 256f, 256f / 256f, 0), false, 0), eventBus);
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

        RenderModule<Ver0Data> module0 = new Ver0Module(glContext);
        RenderModule<Ver1Data> module1 = new Ver1Module(glContext);
        RenderModule<Ver2Data> module2 = new Ver2Module(glContext);

        world.inject(module0);
        world.inject(module1);
        world.inject(module2);
        render.add(module0);
        render.add(module1);
        render.add(module2);

        //bullshiting
        String model0 = ModelUtils.simpleQuad(module0);
        String texmodel = ModelUtils.texturedQuad(module1);
        String objectModel = module2.load(new Ver2Data("objects/dragon.obj"));

        Archetype base = Archetypes.base.build(world);

        ComponentMapper<Position> mapper = world.getMapper(Position.class);

        int texEnt = world.create(base);
        module1.bind(texmodel, texEnt);
        Position positionTex = mapper.get(texEnt);

        positionTex.position.x = 1;
        positionTex.position.y = 0;
        positionTex.position.z = -21f;

        int casEnt = world.create(base);
        module2.bind(objectModel, casEnt);
        Position positionCas = mapper.get(casEnt);

        positionCas.position.x = -1;
        positionCas.position.y = 0;
        positionCas.position.z = -20f;
        //----------------------------------------

        createCamera(tagManager, world);

        glContext.getLightBlock().loadPosition(new Vector3f(0, 1000, -10));
        glContext.getLightBlock().loadDiffuse(new Vector3f(1, 0, 1));

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
                    glContext.close();
                })
                .createLoop()
                .run();
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

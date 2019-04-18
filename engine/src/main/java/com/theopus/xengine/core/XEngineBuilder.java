package com.theopus.xengine.core;

import java.io.Closeable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joml.Vector4f;

import com.artemis.BaseSystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.ecs.systems.CameraSystem;
import com.theopus.xengine.core.ecs.systems.EventSystem;
import com.theopus.xengine.core.ecs.systems.LightSystem;
import com.theopus.xengine.core.ecs.systems.ModelMatrixSystem;
import com.theopus.xengine.core.ecs.systems.MoveSystem;
import com.theopus.xengine.core.ecs.systems.ProjectionSystem;
import com.theopus.xengine.core.ecs.systems.RenderSystem;
import com.theopus.xengine.core.ecs.systems.scipting.ExecutingEngineContext;
import com.theopus.xengine.core.ecs.systems.scipting.JavaExecutingSystem;
import com.theopus.xengine.core.ecs.systems.scipting.JavaScriptExecutingSystem;
import com.theopus.xengine.core.events.EventBus;
import com.theopus.xengine.core.platform.GlfwPlatformManager;
import com.theopus.xengine.core.platform.PlatformManager;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.BaseRenderer;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.core.render.GlRenderer;
import com.theopus.xengine.core.render.RenderModule;
import com.theopus.xengine.core.utils.Reflection;
import com.theopus.xengine.core.utils.WorldAwareCachedInjector;
import com.theopus.xengine.wrapper.glfw.WindowConfig;

/**
 * Invokes all init methods before run.
 */
public class XEngineBuilder {

    List<Closeable> closeables = new ArrayList<>();
    private Set<Object> events = new LinkedHashSet<>();
    private Set<Class<? extends RenderModule<?>>> modules = new LinkedHashSet<>();
    private Set<Class<? extends BaseSystem>> systems = new LinkedHashSet<>(Arrays.asList(
            TagManager.class,
            EventSystem.class,
            JavaExecutingSystem.class,
            JavaScriptExecutingSystem.class,
            RenderSystem.class,
            CustomGroupManager.class,
            CameraSystem.class,
            ProjectionSystem.class,
            MoveSystem.class,
            ModelMatrixSystem.class,
            LightSystem.class
    ));

    public XEngine build() {
        List<Object> contextObjects = new ArrayList<>();

        List<BaseSystem> systems = createSystems(this.systems);

        EventBus eventBus = new EventBus();
        EntityFactory factory = new EntityFactory();
        ExecutingEngineContext context = new ExecutingEngineContext();

        PlatformManager platformManager = new GlfwPlatformManager(
                new WindowConfig(600, 400,
                        new Vector4f(128f / 256f, 128f / 256f, 128f / 256f, 0), false, 0));

        contextObjects.add(platformManager);
        BaseRenderer baseRenderer = glRenderer(modules, contextObjects);
        contextObjects.add(eventBus);
        contextObjects.add(baseRenderer);
        contextObjects.add(context);
        contextObjects.add(factory);
        contextObjects.addAll(baseRenderer.modules());


        WorldConfigurationBuilder configurationBuilder = new WorldConfigurationBuilder();
        systems.forEach(configurationBuilder::with);

        WorldConfiguration configuration = configurationBuilder.build();
        configuration.setInjector(new WorldAwareCachedInjector());
        configuration.register("renderer", baseRenderer);
        contextObjects.forEach(configuration::register);

        World world = new World(configuration);

        contextObjects.forEach(eventBus::subscribe);
        systems.forEach(eventBus::subscribe);

        contextObjects.forEach(world::inject);

        systems.forEach(this::invokeInit);
        contextObjects.forEach(this::invokeInit);

        events.forEach(eventBus::post);
        return new XEngine(world, platformManager, closeables);
    }

    private void invokeInit(Object instance) {
        for (Method method : instance.getClass().getMethods()) {
            if (method.getName().equals("init")) {
                try {
                    method.invoke(instance);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private BaseRenderer glRenderer(Collection<Class<? extends RenderModule<?>>> modules, List<Object> contextObjects) {
        GLContext glContext = new GLContext();
        BaseRenderer render = new GlRenderer(glContext);
        contextObjects.add(glContext);
        contextObjects.add(render);

        for (Class<? extends RenderModule<?>> m : modules) {
            ArtemisRenderModule module = (ArtemisRenderModule) Reflection.newInstance(m);
            Class<?> superclass = module.getClass().getSuperclass();
            render.add((Class<RenderModule<?>>) superclass, module);
            contextObjects.add(module);
        }

        closeables.add(glContext);
        return render;
    }

    public XEngineBuilder withModule(Class<? extends RenderModule<?>> module) {
        modules.add(module);
        return this;
    }

    public XEngineBuilder withSystem(Class<? extends BaseSystem> module) {
        systems.add(module);
        return this;
    }

    private List<BaseSystem> createSystems(Collection<Class<? extends BaseSystem>> systems) {
        return systems.stream().map(Reflection::newInstance).collect(Collectors.toList());
    }

    public XEngineBuilder withEvent(Object event) {
        events.add(event);
        return this;
    }
}

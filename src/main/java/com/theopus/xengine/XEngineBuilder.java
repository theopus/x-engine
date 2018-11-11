package com.theopus.xengine;

import com.google.common.base.Preconditions;
import com.theopus.xengine.ecs.*;
import com.theopus.xengine.ecs.system.BaseSystem;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.inject.TaskConfigurer;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.event.EventManager;
import com.theopus.xengine.event.EventProvider;
import com.theopus.xengine.nscheduler.task.TaskChain;
import com.theopus.xengine.platform.GlfwPlatformManager;
import com.theopus.xengine.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.Feeder;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.render.Render;
import com.theopus.xengine.render.RenderConfig;
import com.theopus.xengine.render.RenderModule;
import com.theopus.xengine.utils.TaskUtils;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class XEngineBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(XEngineBuilder.class);
    private Map<Class<?>, Object> ctx = new HashMap<>();

    private SystemsConfig systemsConfig;
    private RenderConfig rerenderConfig;

    private Class<? extends PlatformManager> pm = GlfwPlatformManager.class;

    public static XEngineBuilder create() {
        return new XEngineBuilder();
    }

    public XEngineBuilder ecs(EntitySystemConfig ecsConfig) {
        ctx.put(EntitySystemConfig.class, ecsConfig);
        return this;
    }

    public XEngineBuilder systems(SystemsConfig systems) {
        this.systemsConfig = systems;
        return this;
    }

    public XEngineBuilder platformManager(Class<? extends PlatformManager> pm, WindowConfig windowConfig) {
        this.pm = pm;
        ctx.put(WindowConfig.class, Preconditions.checkNotNull(windowConfig));
        return this;
    }

    public XEngineBuilder feeder(Feeder feeder) {
        ctx.put(Feeder.class, Preconditions.checkNotNull(feeder, "Feeder is null"));
        return this;
    }


    public XEngineBuilder render(RenderConfig renderClass) {
        this.rerenderConfig = renderClass;
        return this;
    }


    public XEngine build() {
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_LOADER.set(true);

        pm = Preconditions.checkNotNull(pm, "Platform manager class is null");

        Scheduler scheduler = createInstance(Scheduler.class);
        EventManager em = createInstance(EventManager.class);
        PlatformManager plm = createInstance(pm);
        EntitySystemManager ecm = createInstance(EntitySystemManager.class);

        plm.createWindow();
        Render render = createRender(this.rerenderConfig);

        Map<? extends BaseSystem, Task> systemsTasks = Stream.of(systemsConfig.getSystems())
                .map(this::createInstance)
                .collect(Collectors.toMap(s -> s, BaseSystem::task));

        EcsProvider ecmProvider = ecm.getProvider();
        EventProvider emProvider = em.getProvider();
        TaskConfigurer taskConfigurer = new TaskConfigurer(ecmProvider, emProvider);
        configureSystems(taskConfigurer, systemsTasks);


        List<Task> tasks = new ArrayList<>();

        tasks.addAll(systemsTasks.values());
        tasks.add(em.task(10));

        Task head = TaskChain
                .startWith(TaskUtils.initCtx(plm, Context.MAIN))
                .andThen(TaskUtils.initCtx(plm, Context.SIDE))
                .andThen(new Task() {
                    @Override
                    public void process() {
                        tasks.forEach(scheduler::propose);
                    }
                }).head();

        scheduler.propose(head);
        return new XEngine(scheduler, em, plm, ecm, render);
    }

    private void configureSystems(TaskConfigurer configurer, Map<? extends BaseSystem, Task> systemsTasks) {
        systemsTasks.forEach(configurer::configure);
        systemsTasks.keySet().forEach(BaseSystem::init);
    }

    private <T> T createInstance(Class<T> create) {
        return createInstance(create, create);
    }

    private <T> T createInstance(Class<? extends T> create, Class<T> superclass) {

        Constructor<?> constructor;
        if (create.getDeclaredConstructors().length == 0) {
            constructor = create.getDeclaredConstructors()[0];
        } else {
            constructor = Stream.of(create.getDeclaredConstructors())
                    .filter(c -> c.isAnnotationPresent(Inject.class))
                    .findAny()
                    .orElseThrow(() -> new RuntimeException(" Not found constructor annotated with Inject in `" + create));
        }


        List<Object> objects = new ArrayList<>();
        Class<?>[] parameterTypes = constructor.getParameterTypes();

        for (Class<?> parameterType : parameterTypes) {
            LOGGER.info("{}", parameterType);

            Object o = ctx.get(ctx.keySet()
                    .stream()
                    .filter(parameterType::isAssignableFrom).findAny()
                    .orElseThrow(() -> new RuntimeException("Not found in ctx " + parameterType)));
            objects.add(o);
        }

        try {
            LOGGER.info("Found {}", objects);
            LOGGER.info("Applying to {}", Arrays.toString(constructor.getParameterTypes()));
            T t = (T) constructor.newInstance(objects.toArray());
            ctx.put(superclass, t);
            LOGGER.info("Ctxted as {}", superclass);
            LOGGER.info("Current ctx {}", ctx);
            return t;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Render createRender(RenderConfig config) {
        Map<Class<? extends RenderModule>, Class<? extends RenderModule>> modules = config.getModules();
        try {
            Map<Class<? extends RenderModule>, RenderModule> arg = new HashMap<>();
            for (Map.Entry<Class<? extends RenderModule>, Class<? extends RenderModule>> ci : modules.entrySet()) {
                arg.put(ci.getKey(), ci.getValue().newInstance());
            }

            Render render = config.getRender().getConstructor(Map.class).newInstance(new Object[]{arg});
            ctx.put(Render.class, render);
            return render;
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}

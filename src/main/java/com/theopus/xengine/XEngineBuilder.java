package com.theopus.xengine;

import com.google.common.base.Preconditions;
import com.theopus.xengine.conc.StateFactory;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.lock.LockFactory;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.platform.GlfwPlatformManager;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.Feeder;
import com.theopus.xengine.render.Render;
import com.theopus.xengine.render.RenderModule;
import com.theopus.xengine.render.opengl.GlRender;
import com.theopus.xengine.system.System;
import com.theopus.xengine.utils.EcsConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Stream;

public class XEngineBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(XEngineBuilder.class);

    private Map<Class<?>, Object> ctx = new HashMap<>();

    private EcsConfig ecsConfig;
    private List<Class<? extends System>> systems;
    private WindowConfig windowConfig;
    private int nStates;

    private Class<? extends PlatformManager> pm = GlfwPlatformManager.class;
    private Class<? extends Render> renderClass = GlRender.class;
    private List<Class<? extends RenderModule>> modules;

    public static XEngineBuilder create() {
        return new XEngineBuilder();
    }

    public XEngineBuilder ecs(EcsConfig ecsConfig) {
        this.ecsConfig = ecsConfig;
        ctx.put(EcsConfig.class, ecsConfig);
        return this;
    }

    public XEngineBuilder systems(List<Class<? extends System>> systems) {
        this.systems = systems;
        return this;
    }

    public XEngineBuilder platformManager(Class<? extends PlatformManager> pm, WindowConfig windowConfig) {
        this.pm = pm;
        ctx.put(WindowConfig.class, Preconditions.checkNotNull(windowConfig));
        return this;
    }

    public XEngineBuilder statesCount(int nStates) {
        this.nStates = nStates;
        this.ctx.put(int.class, nStates);
        return this;
    }

    public XEngineBuilder feeder(Feeder feeder) {
        ctx.put(Feeder.class, Preconditions.checkNotNull(feeder, "Feeder is null"));
        return this;
    }


    public XEngineBuilder render(Class<? extends Render> renderClass) {
        this.renderClass = Preconditions.checkNotNull(renderClass);
        return this;
    }


    public XEngineBuilder modules(List<Class<? extends RenderModule>> modules) {
        this.modules = modules;
        return this;
    }

    public XEngine build() {
        pm = Preconditions.checkNotNull(pm, "Platform manager class is null");

        createInstance(Scheduler.class);
        createInstance(EventManager.class);
        createInstance(pm);
        createInstance(StateFactory.class, LockFactory.class);
        createInstance(LockManager.class);

        return new XEngine(ctx);
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
}

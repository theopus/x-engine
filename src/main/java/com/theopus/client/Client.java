package com.theopus.client;

import com.google.common.collect.ImmutableMap;
import com.theopus.client.ecs.system.PlaygroundVer0System;
import com.theopus.client.ecs.system.RenderSystem;
import com.theopus.client.ecs.system.TestSystem;
import com.theopus.client.ecs.system.UpdateSystem;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.RenderTrait;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.client.render.Ver0Module;
import com.theopus.client.render.Ver0ModuleImpl;
import com.theopus.xengine.WindowConfig;
import com.theopus.xengine.XEngine;
import com.theopus.xengine.XEngineBuilder;
import com.theopus.xengine.ecs.EntitySystemConfig;
import com.theopus.xengine.ecs.SystemsConfig;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.platform.GlfwPlatformManager;
import com.theopus.xengine.render.RenderConfig;
import com.theopus.xengine.render.opengl.GlRender;
import org.joml.Vector4f;

import java.util.Arrays;

public class Client {

    public static void main(String[] args) throws Exception {

        WindowConfig windowConfig = new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0);


        XEngine engine = XEngineBuilder.
                create()
                .ecs(new EntitySystemConfig(5, Arrays.asList(
                        PositionTrait.class, RenderTrait.class, WorldPositionTrait.class
                )))
                .systems(new SystemsConfig(
//                        TestSystem.class,
                        PlaygroundVer0System.class,
                        RenderSystem.class,
                        UpdateSystem.class
                        ))
                ///
                .platformManager(GlfwPlatformManager.class, windowConfig)
                .feeder(new ExecutorServiceFeeder())
                .render(new RenderConfig(GlRender.class, ImmutableMap.of(Ver0Module.class, Ver0ModuleImpl.class)))
                .build();

        engine.run();
    }
}

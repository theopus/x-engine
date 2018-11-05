package com.theopus.client;

import com.google.common.collect.ImmutableMap;
import com.theopus.client.ecs.system.RenderSystem;
import com.theopus.client.ecs.system.UpdateSystem;
import com.theopus.client.ecs.trait.*;
import com.theopus.client.render.Ver0Module;
import com.theopus.xengine.WindowConfig;
import com.theopus.xengine.XEngine;
import com.theopus.xengine.XEngineBuilder;
import com.theopus.xengine.nscheduler.platform.GlfwPlatformManager;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.render.opengl.GlRender;
import com.theopus.xengine.utils.EcsConfig;
import org.joml.Vector4f;

import java.util.Arrays;

public class Client {

    public static void main(String[] args) {

        WindowConfig windowConfig = new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0);


        XEngine engine = XEngineBuilder.
                create()
                .ecs(new EcsConfig(ImmutableMap.of(
                        RenderTrait.class, WorldPositionTraitEditor.class,
                        WorldPositionTrait.class, WorldPositionTraitEditor.class,
                        PositionTrait.class, PositionTraitEditor.class
                )))
                .systems(Arrays.asList(RenderSystem.class, UpdateSystem.class))
                .platformManager(GlfwPlatformManager.class, windowConfig)
                .statesCount(3)
                .feeder(new ExecutorServiceFeeder())
                .render(GlRender.class).modules(Arrays.asList(Ver0Module.class))
                .build();

        engine.run();
    }
}

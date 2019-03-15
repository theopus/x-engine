package com.theopus.xengine.core;

import com.artemis.*;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.components.Camera;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Velocity;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.ecs.systems.*;
import com.theopus.xengine.core.ecs.systems.scipting.ExecutingEngineContext;
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
import com.theopus.xengine.core.render.modules.v3.Ver3Data;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;
import com.theopus.xengine.core.utils.WorldAwareCachedInjector;
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

    private final List<Closeable> closeables;
    private final World world;
    private final PlatformManager platformManager;

    public XEngine(World world, PlatformManager platformManager, List<Closeable> closeables) {
        this.world = world;
        this.platformManager = platformManager;
        this.closeables = closeables;
    }

    public void run() throws IOException {
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
}

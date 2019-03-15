package com.theopus.xengine.core;

import com.artemis.*;
import com.theopus.xengine.core.platform.PlatformManager;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;

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

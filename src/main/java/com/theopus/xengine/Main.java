package com.theopus.xengine;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.system.RenderSystem;
import com.theopus.xengine.system.UpdateSystem;
import com.theopus.xengine.trait.*;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, IOException, InstantiationException, IllegalAccessException {
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_LOADER.set(true);

        WindowManager wm = new WindowManager(
                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
                new GLFWKeyCallback() {
                    @Override
                    public void invoke(long window, int key, int scancode, int action, int mods) {

                    }
                }

        );

        wm.createWindow();
        wm.showWindow();

        EntityManagerFactory factory = new EntityManagerFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        ));

        StateManager stateManager = new StateManager(factory, 3);
        Scheduler scheduler = new Scheduler(stateManager);




        State state = stateManager.forWrite();


        int e = state.getEm().createEntity();


        RenderTrait trait = state.getMapper(RenderTrait.class).get(e);

        RenderTraitLoader renderTraitLoader = new RenderTraitLoader();


        RenderTrait renderTrait = renderTraitLoader.loadEntity(
                trait,
                new float[]{
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,

                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,

                }, 6
        );




        wm.deatachContext();

        RenderSystem renderSystem = new RenderSystem(
                wm, scheduler
        );
        UpdateSystem updateSystem = new UpdateSystem();


        stateManager.release(state);

        scheduler.propose(updateSystem.task());

        scheduler.propose(renderSystem.prepareTask());


        RateLimiter limiter = RateLimiter.create(1024);

        while (!wm.windowShouldClose()) {
            limiter.acquire();
            scheduler.operate();
            wm.update();
        }

        scheduler.drain();
        scheduler.setAccepting(true);

        scheduler.propose(renderSystem.closeTask());
        scheduler.operate();
        scheduler.operate();
        scheduler.operate();
        scheduler.drain();


        wm.attachMainContext();
        renderTraitLoader.cleanup();
        scheduler.close();
        wm.close();
    }
}

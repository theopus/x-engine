package com.theopus.xengine;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.system.InputSystem;
import com.theopus.xengine.system.RenderSystem;
import com.theopus.xengine.system.UpdateSystem;
import com.theopus.xengine.trait.EntityManagerFactory;
import com.theopus.xengine.trait.State;
import com.theopus.xengine.trait.StateManager;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.custom.RenderTraitEditor;
import org.joml.Vector3f;
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

        RenderTrait renderTrait1 = state.getMapper(RenderTrait.class).get(1);
        renderTrait.duplicateTo(renderTrait1);
        RenderTrait renderTrait2 = state.getMapper(RenderTrait.class).get(2);
        renderTrait.duplicateTo(renderTrait2);
        RenderTrait renderTrait3 = state.getMapper(RenderTrait.class).get(3);
        renderTrait.duplicateTo(renderTrait3);

        PositionTrait positionTrait0 = state.getMapper(PositionTrait.class).get(0);

        PositionTrait positionTrait1 = state.getMapper(PositionTrait.class).get(1);
        positionTrait1.setPosition(new Vector3f(-1, -1, 0));
        PositionTrait positionTrait2 = state.getMapper(PositionTrait.class).get(2);
        positionTrait2.setPosition(new Vector3f(1, -1, 0));
        PositionTrait positionTrait3 = state.getMapper(PositionTrait.class).get(3);
        positionTrait3.setPosition(new Vector3f(1, 1, 0));

        wm.deatachContext();

        RenderSystem renderSystem = new RenderSystem(
                wm, scheduler
        );
        UpdateSystem updateSystem = new UpdateSystem();


        stateManager.release(state);

        scheduler.propose(updateSystem.task());

        scheduler.propose(renderSystem.prepareTask());

        InputSystem inputSystem = new InputSystem();

        wm.setCallbacK(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == 1 || action == 0) {
                    scheduler.propose(inputSystem.task(key, action));
                }
            }
        });

        RateLimiter limiter = RateLimiter.create(2048);

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

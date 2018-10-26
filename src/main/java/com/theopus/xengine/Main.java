package com.theopus.xengine;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.conc.State;
import com.theopus.xengine.conc.StateFactory;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.input.GlfwInput;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.platform.GlfwPlatformManager;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.nscheduler.task.TaskChain;
import com.theopus.xengine.nscheduler.task.TaskFactory;
import com.theopus.xengine.opengl.RenderTraitLoader;
import com.theopus.xengine.system.InputSystem;
import com.theopus.xengine.system.RenderSystem;
import com.theopus.xengine.system.UpdateSystem;
import com.theopus.xengine.trait.EntityManager;
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

import java.util.concurrent.ExecutionException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException, ExecutionException {
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_LOADER.set(true);

        GlfwPlatformManager pm = new GlfwPlatformManager(
                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
                new GLFWKeyCallback() {
                    @Override
                    public void invoke(long window, int key, int scancode, int action, int mods) {

                    }
                }

        );
        LockManager<State> lm = new LockManager<State>(new StateFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        )), 3);

        EventManager em = new EventManager(ImmutableMap.of());

        pm.createWindow();
        pm.showWindow();




        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());


        Lock<State> lock = lm.forWrite();

        EntityManager manager = lock.getOf().getManager();


        int e = manager.createEntity();


        RenderTrait trait = manager.getMapper(RenderTrait.class).get(e);

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

        RenderTrait renderTrait1 = manager.getMapper(RenderTrait.class).get(1);
        renderTrait.duplicateTo(renderTrait1);
        RenderTrait renderTrait2 = manager.getMapper(RenderTrait.class).get(2);
        renderTrait.duplicateTo(renderTrait2);
        RenderTrait renderTrait3 = manager.getMapper(RenderTrait.class).get(3);
        renderTrait.duplicateTo(renderTrait3);

        PositionTrait positionTrait0 = manager.getMapper(PositionTrait.class).get(0);

        PositionTrait positionTrait1 = manager.getMapper(PositionTrait.class).get(1);
        positionTrait1.setPosition(new Vector3f(-1, -1, 0));
        PositionTrait positionTrait2 = manager.getMapper(PositionTrait.class).get(2);
        positionTrait2.setPosition(new Vector3f(1, -1, 0));
        PositionTrait positionTrait3 = manager.getMapper(PositionTrait.class).get(3);
        positionTrait3.setPosition(new Vector3f(1, 1, 0));

        pm.detachContext();

        RenderSystem renderSystem = new RenderSystem(pm);
        UpdateSystem updateSystem = new UpdateSystem();


        lm.release(lock);

        TaskFactory<State> factory = new TaskFactory<>(lm, em, pm.getInput());

        TaskChain taskChain = TaskChain.startWith(renderSystem.prepareTask()).andThen(renderSystem.renderTask()).andThen(renderSystem.closeTask());
        scheduler.propose(factory.injectManagers(taskChain).head());
        scheduler.propose(factory.injectManagers(updateSystem.task()));

        InputSystem inputSystem = new InputSystem();

        pm.setCallback(new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == 1 || action == 0) {
                    scheduler.propose(factory.injectManagers(inputSystem.task(key, action)));
                }
            }
        });

        RateLimiter limiter = RateLimiter.create(4096);

        while (!pm.shouldClose()) {
//            limiter.acquire();
            scheduler.process();
            pm.processEvents();
        }

        scheduler.drain();

        pm.attachContext(Context.MAIN);

        renderTraitLoader.cleanup();

        scheduler.close();

        pm.close();
    }
}

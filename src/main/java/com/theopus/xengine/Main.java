package com.theopus.xengine;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.conc.State;
import com.theopus.xengine.conc.StateFactory;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.event.Event;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.event.InputData;
import com.theopus.xengine.nscheduler.event.TopicWriter;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.platform.GlfwPlatformManager;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.nscheduler.task.TaskChain;
import com.theopus.xengine.nscheduler.task.TaskConfigurer;
import com.theopus.xengine.nscheduler.task.TaskFactory;
import com.theopus.xengine.opengl.RenderTraitLoader;
import com.theopus.xengine.system.InputSystem;
import com.theopus.xengine.system.RenderSystem;
import com.theopus.xengine.system.UpdateSystem;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.custom.RenderTraitEditor;
import com.theopus.xengine.utils.PlayGround;
import com.theopus.xengine.utils.TaskUtils;
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
        //scheduler
        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());
        //loader
        RenderTraitLoader loader = new RenderTraitLoader();
        //Platform system
        GlfwPlatformManager pm = new GlfwPlatformManager(
                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
                new GLFWKeyCallback() {
                    @Override
                    public void invoke(long window, int key, int scancode, int action, int mods) {

                    }
                }
        );
        pm.createWindow();
        pm.showWindow();
        //lock & entities & systems
        LockManager<State> lm = new LockManager<State>(new StateFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        )), 3);

        RenderSystem renderSystem = new RenderSystem(pm);
        UpdateSystem updateSystem = new UpdateSystem();
        InputSystem inputSystem = new InputSystem();
        //events
        EventManager em = new EventManager(ImmutableMap.of(
                EventManager.Topics.INPUT_DATA_TOPIC.getId(), EventManager.Topics.INPUT_DATA_TOPIC
        ));

        //tasks
        TaskConfigurer configurer = new TaskConfigurer(lm, em, pm.getInput());
        TaskChain taskChain = configurer.injectManagers(
                TaskChain
                        .startWith(TaskUtils.initCtx(pm, Context.MAIN))
                        .andThen(TaskUtils.initCtx(pm, Context.SIDE))
                        .andThen(PlayGround.ver0(loader))
                        //update chain split
                        .onFinish(updateSystem.task())
                        //back to render
                        .andThen(renderSystem.prepareTask())
                        .andThen(renderSystem.renderTask())
                        //sequential teardown tasks
                        .andThen(renderSystem.closeTask())
                        .andThen(TaskUtils.close(Context.SIDE, loader))
                        .andThen(TaskUtils.close(Context.MAIN, pm)));

        scheduler.propose(taskChain.head());


        TopicWriter<InputData> input = em.createWriter(EventManager.Topics.INPUT_DATA_TOPIC);
        pm.detachContext();
        pm.setCallback(new GLFWKeyCallback() {

            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == 1 || action == 0) {
//                    scheduler.propose(factory.injectManagers(inputSystem.task(key, action)));
                    input.write(new Event<>(new InputData(key, action)));

                }
            }
        });


        //main loop
        RateLimiter limiter = RateLimiter.create(100_000_000);
        while (!pm.shouldClose()) {
            limiter.acquire();
            scheduler.process();
            input.prepare();
            pm.processEvents();
            input.finish();
            em.trimTo(60);
        }

        //teardown
        scheduler.close();
    }
}

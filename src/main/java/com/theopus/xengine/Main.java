package com.theopus.xengine;

import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.conc.State;
import com.theopus.xengine.conc.StateFactory;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.platform.GlfwPlatformManager;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.nscheduler.task.TaskChain;
import com.theopus.xengine.inject.TaskConfigurer;
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
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException, ExecutionException, NoSuchFieldException {
        //config
        Configuration.DEBUG.set(true);
        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
        Configuration.DEBUG_LOADER.set(true);
        //scheduler
        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());
        //loader
        RenderTraitLoader loader = new RenderTraitLoader();
        //events
        EventManager em = new EventManager(ImmutableMap.of(
                EventManager.Topics.INPUT_DATA_TOPIC.getId(), EventManager.Topics.INPUT_DATA_TOPIC
        ));
        //Platform system
        PlatformManager pm = new GlfwPlatformManager(
                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
                em.createWriter(EventManager.Topics.INPUT_DATA_TOPIC)

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


        //tasks
        TaskConfigurer configurer = new TaskConfigurer(lm, em, pm.getInput());

        ComponentTask updateTask = updateSystem.task();
        configurer.inj(updateSystem, updateTask);

        TaskChain taskChain = configurer.injectManagers(
                TaskChain
                        .startWith(TaskUtils.initCtx(pm, Context.MAIN))
                        //fork for event trim
                        .onFinish(em.task(5))
                        //back to chain
                        .andThen(TaskUtils.initCtx(pm, Context.SIDE))
                        .andThen(PlayGround.ver0(loader))
                        //update chain split
                        .onFinish(updateTask)
                        //back to render
                        .andThen(renderSystem.prepareTask())
                        .andThen(renderSystem.renderTask())
                        //sequential teardown tasks
                        .andThen(renderSystem.closeTask())
                        .andThen(TaskUtils.close(Context.SIDE, loader))
                        .andThen(TaskUtils.close(Context.MAIN, pm)));

        scheduler.propose(taskChain.head());

        pm.detachContext();

        //main loop
//        RateLimiter limiter = RateLimiter.create(1);
        RateLimiter limiter = RateLimiter.create(100_000_000);

        while (!pm.shouldClose()) {
            //process platform layer
            pm.processEvents();
            //throttle mainloop
            limiter.acquire();
            //execute scheduler
            scheduler.process();
        }

        //teardown
        scheduler.close();
    }
}

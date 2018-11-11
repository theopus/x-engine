//package com.theopus.xengine;
//
//import com.google.common.collect.ImmutableMap;
//import com.google.common.util.concurrent.RateLimiter;
//import com.theopus.xengine.conc.State;
//import com.theopus.xengine.conc.StateFactory;
//import com.theopus.xengine.inject.TaskConfigurer;
//import com.theopus.xengine.nscheduler.Context;
//import com.theopus.xengine.nscheduler.Scheduler;
//import com.theopus.xengine.event.EventManager;
//import com.theopus.xengine.event.TopicReader;
//import com.theopus.xengine.nscheduler.lock.LockManager;
//import com.theopus.xengine.platform.GlfwPlatformManager;
//import com.theopus.xengine.platform.PlatformManager;
//import com.theopus.xengine.nscheduler.task.ComponentTask;
//import com.theopus.xengine.nscheduler.task.ExecutorServiceFeeder;
//import com.theopus.xengine.nscheduler.task.TaskChain;
//import com.theopus.xengine.opengl.SimpleLoader;
//import com.theopus.client.ecs.trait.PositionTrait;
//import com.theopus.client.ecs.trait.PositionTraitEditor;
//import com.theopus.client.ecs.trait.WorldPositionTrait;
//import com.theopus.client.ecs.trait.WorldPositionTraitEditor;
//import com.theopus.xengine.utils.EcsConfig;
//import com.theopus.xengine.utils.PlayGround;
//import com.theopus.xengine.utils.TaskUtils;
//import org.joml.Vector2i;
//import org.joml.Vector4f;
//import org.lwjgl.system.Configuration;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ExecutionException;
//
//public class Main {
//    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
//
//    public static void main(String[] args) throws InterruptedException, InstantiationException, IllegalAccessException, ExecutionException, NoSuchFieldException, IOException {
//        //config
//        Yaml yaml = new Yaml();
//        Map<String, String> load = yaml.load(Main.class.getClassLoader().getResourceAsStream("config.yaml"));
//
//        Configuration.DEBUG.set(true);
//        Configuration.DEBUG_MEMORY_ALLOCATOR.set(true);
//        Configuration.DEBUG_LOADER.set(true);
//        //scheduler
//        Scheduler scheduler = new Scheduler(new ExecutorServiceFeeder());
//        //loader
//        SimpleLoader loader = new SimpleLoader();
//        //events
//        EventManager em = new EventManager(scheduler);
//        //Platform system
//        PlatformManager pm = new GlfwPlatformManager(
//                new WindowConfig(600, 400, new Vector4f(0.8f, 0.8f, 0.8f, 1), false, 0),
//                em
//        );
//        pm.createWindow();
//        pm.showWindow();
//        //lock & entities & systems
//        LockManager<State> lm = new LockManager<State>(new StateFactory(new EcsConfig(ImmutableMap.of(
//                WorldPositionTrait.class, WorldPositionTraitEditor.class,
//                PositionTrait.class, PositionTraitEditor.class
//        ))), 3);
//
//        RenderSystem renderSystem = new RenderSystem(pm);
//        UpdateSystem updateSystem = new UpdateSystem();
//        InputSystem inputSystem = new InputSystem();
//
//        //tasks
//        TaskConfigurer configurer = new TaskConfigurer(lm, em, pm.getInput());
//
//        ComponentTask updateTask = updateSystem.task();
//        ComponentTask renderTask = renderSystem.task();
//
//        TaskChain taskChain = configurer.injectManagers(
//                TaskChain
//                        .startWith(TaskUtils.initCtx(pm, Context.MAIN))
//                        //fork for event trim
//                        .onFinish(em.task(5))
//                        //back to chain
//                        .andThen(TaskUtils.initCtx(pm, Context.SIDE))
//                        .andThen(renderSystem.prepareTask())
//                        .andThen(PlayGround.ver0(renderSystem))
//                        //update chain split
//                        .onFinish(updateTask)
//                        //back to render
//                        .andThen(renderTask)
//                        //sequential teardown tasks
//                        .andThen(renderSystem.closeTask())
//                        .andThen(TaskUtils.close(Context.SIDE, loader))
//                        .andThen(TaskUtils.close(Context.MAIN, pm)));
//
//        scheduler.propose(taskChain.head());
//
//        configurer.inj(updateSystem, updateTask);
//        configurer.inj(renderSystem, renderTask);
//        pm.detachContext();
//
//        //main loop
////        RateLimiter limiter = RateLimiter.create(1);
//        RateLimiter limiter = RateLimiter.create(100_000_000);
//
//        //event listeners
//        TopicReader<Vector2i> reader = em.createReader(EventManager.Topics.FRAMEBUFFER_CHANGED);
//        em.listen(renderSystem.frameBufferRefresh(reader), reader);
//
//        while (!pm.shouldClose()) {
//            //process platform layer
//            pm.processEvents();
//            //throttle mainloop
//            limiter.acquire();
//            //execute scheduler
//            scheduler.process();
//        }
//
//        //teardown
//        scheduler.close();
//    }
//}

package com.theopus.xengine;

import com.theopus.xengine.entity.EntityManager;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.system.RenderSystem;
import com.theopus.xengine.system.UpdateSystem;
import com.theopus.xengine.trait.PositionTrait;
import com.theopus.xengine.trait.RenderTrait;
import com.theopus.xengine.utils.OpsCounter;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.system.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

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

        EntityManager entityManager = new EntityManager();

        int e = entityManager.createEntity();
        RenderTrait trait = entityManager.createTrait(e, RenderTrait.class);

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

        Scheduler scheduler = new Scheduler();


        wm.deatachContext();

        Entity entity = new Entity(renderTrait, new PositionTrait());

        RenderSystem renderSystem = new RenderSystem(
                wm, scheduler, renderTrait
        );

        UpdateSystem updateSystem = new UpdateSystem(wm, entity);


        scheduler.propose(renderSystem.prepareTask());
        scheduler.propose(updateSystem.task());


        while (!wm.windowShouldClose()) {
            scheduler.operate();
            wm.update();
        }

        Thread.sleep(10000);
        scheduler.propose(renderSystem.closeTask());


        wm.attachMainContext();
        renderTraitLoader.cleanup();
        wm.close();
    }
}

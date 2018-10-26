package com.theopus.xengine.system;

import com.theopus.xengine.conc.SystemRTask;
import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.opengl.Render;
import com.theopus.xengine.opengl.StaticShader;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.utils.OpsCounter;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class RenderSystem extends EntitySystem {

    private final OpsCounter fps;
    private TraitMapper<RenderTrait> renderMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystem.class);

    private PlatformManager pm;
    private Render render;


    public RenderSystem(PlatformManager pm) {
        super(RenderTrait.class);
        this.pm = pm;
        fps = new OpsCounter("FrameRender");
    }

    @Override
    public void process(IntStream entities) {
        entities.forEach(id -> {
            RenderTrait renderTrait = renderMapper.get(id);
            render.render(renderTrait);
        });
        fps.operateAndLog();
        pm.refreshWindow();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//
//        try {
//            //TODO fake render time
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    void injectEm(EntityManager em) {
        this.renderMapper = em.getMapper(RenderTrait.class);
    }

    public Task renderTask() {
        return new SystemRTask(Context.MAIN, true, Integer.MAX_VALUE, this);
    }

    public Task prepareTask() {
        return new SystemRWTask(Context.MAIN, false, this) {
            @Override
            public void process() throws Exception {
                LOGGER.info("Preparing");
                pm.attachContext(Context.MAIN);
                StaticShader staticShader = new StaticShader("static.vert", "static.frag");
                render = new Render(staticShader);
            }
        };
    }


    public Task closeTask() {
        return new SystemRWTask(Context.MAIN, false, this) {

            @Override
            public void process() {
                LOGGER.info("Closing RenderTask.");
                render.cleanup();
                pm.detachContext();
            }
        };
    }
}

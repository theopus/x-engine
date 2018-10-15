package com.theopus.xengine.system;

import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.Render;
import com.theopus.xengine.StaticShader;
import com.theopus.xengine.WindowManager;
import com.theopus.xengine.scheduler.EntitesTask;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.xengine.utils.OpsCounter;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.IntStream;

public class RenderSystem implements System {

    private final OpsCounter fps;
    private TraitMapper<RenderTrait> renderMapper;
    private RenderSystemConfigurer configurer;

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystem.class);

    private WindowManager wm;
    private Render render;
    private final Scheduler scheduler;


    public RenderSystem(WindowManager wm, Scheduler scheduler) {
        this.wm = wm;
        this.scheduler = scheduler;
        this.configurer = new RenderSystemConfigurer(this);
        fps = new OpsCounter("FrameRender");
    }

    @Override
    public void process(IntStream entities) {
        entities.forEach(id -> {
            RenderTrait renderTrait = renderMapper.get(id);
            render.render(renderTrait);
        });
        wm.printGLErrors();
        fps.operateAndLog();
        wm.swapBuffers();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Configurer configurer() {
        return configurer;
    }

    public SchedulerTask prepareTask() {
        return new RenderTask(false) {

            @Override
            public void run() {
                LOGGER.info("Started prepearing RenderSystem");

                wm.attachMainContext();

                StaticShader staticShader = null;
                try {
                    staticShader = new StaticShader("static.vert", "static.frag");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                render = new Render(staticShader);
                scheduler.propose(renderTask());
            }
        }.setSystem(this);
    }


    public SchedulerTask renderTask() {
        return new RenderEntityTask(this).setRateLimiter(RateLimiter.create(Double.MAX_VALUE));
    }

    public SchedulerTask closeTask() {
        return new RenderTask(false) {

            @Override
            public void run() {
                LOGGER.info("Closing RenderTask.");
                render.cleanup();
                wm.deatachContext();
            }
        }.setSystem(this);
    }

    Class[] classes = new Class[]{
            RenderTrait.class
    };

    @Override
    public Class<? extends Trait>[] toPass() {
        return classes;
    }


    public void setRenderMapper(TraitMapper<RenderTrait> renderMapper) {
        this.renderMapper = renderMapper;
    }


    public abstract class RenderTask extends SchedulerTask {

        public RenderTask(boolean repeatable) {
            super(Scheduler.ThreadType.MAIN_CONTEXT, repeatable);
        }
    }

    public class RenderEntityTask extends EntitesTask {

        public RenderEntityTask(System system) {
            super(Scheduler.ThreadType.MAIN_CONTEXT, true, system);
        }
    }
}

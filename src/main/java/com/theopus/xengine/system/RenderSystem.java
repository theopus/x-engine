package com.theopus.xengine.system;

import com.theopus.xengine.Render;
import com.theopus.xengine.StaticShader;
import com.theopus.xengine.WindowManager;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.trait.RenderTrait;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.xengine.utils.OpsCounter;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

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
    public void process() {
        RenderTrait renderTrait = renderMapper.get(0);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        render.render(renderTrait);

        wm.swapBuffers();
        wm.printGLErrors();
        fps.operateAndLog();
    }

    @Override
    public Configurer configurer() {
        return configurer;
    }

    public SchedulerTask prepareTask(){
        return new RenderTask(false){

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


    public SchedulerTask renderTask(){
        return new RenderTask(true){

            @Override
            public void run() {
                process();
            }
        }.setSystem(this);
    }

    public SchedulerTask closeTask(){
        return new RenderTask(false){

            @Override
            public void run() {
                LOGGER.info("Closing RenderTask.");
                render.cleanup();
                wm.deatachContext();
            }
        }.setSystem(this);
    }

    public void setRenderMapper(TraitMapper<RenderTrait> renderMapper) {
        this.renderMapper = renderMapper;
    }


    public abstract class RenderTask extends SchedulerTask{

        public RenderTask(boolean repeatable) {
            super(Scheduler.ThreadType.MAIN_CONTEXT, repeatable);
        }
    }
}

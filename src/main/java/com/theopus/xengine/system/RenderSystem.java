package com.theopus.xengine.system;

import com.theopus.xengine.Render;
import com.theopus.xengine.StaticShader;
import com.theopus.xengine.WindowManager;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.trait.RenderTrait;
import com.theopus.xengine.utils.OpsCounter;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RenderSystem implements System {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystem.class);

    private final OpsCounter fps;
    private WindowManager wm;
    private Render render;
    private final Scheduler scheduler;
    private List<RenderTrait> traits = new ArrayList<>();

    public RenderSystem(WindowManager wm, Scheduler scheduler, RenderTrait ... traitsArr) {
        this.wm = wm;
        this.scheduler = scheduler;
        traits.addAll(Arrays.asList(traitsArr));
        fps = new OpsCounter("FrameRender");
    }

    @Override
    public void process() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        traits.forEach(renderTrait -> render.render(renderTrait));
        wm.swapBuffers();
        wm.printGLErrors();
        fps.operateAndLog();
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
        };
    }


    public SchedulerTask renderTask(){
        return new RenderTask(true){

            @Override
            public void run() {
                process();
            }
        };
    }

    public SchedulerTask closeTask(){
        return new RenderTask(false){

            @Override
            public void run() {
                LOGGER.info("Closing RenderTask.");
                render.cleanup();
                wm.deatachContext();
            }
        };
    }


    public abstract class RenderTask extends SchedulerTask{

        public RenderTask(boolean repeatable) {
            super(Scheduler.ThreadType.MAIN_CONTEXT, repeatable);
        }
    }
}

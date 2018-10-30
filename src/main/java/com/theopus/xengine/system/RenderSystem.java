package com.theopus.xengine.system;

import com.theopus.xengine.conc.SystemRTask;
import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.inject.InjectEvent;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.opengl.BaseRender;
import com.theopus.xengine.opengl.shader.StaticShader;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.utils.OpsCounter;
import org.joml.Vector2i;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class RenderSystem extends EntitySystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystem.class);
    private final OpsCounter fps;
    private TraitMapper<RenderTrait> renderMapper;
    private PlatformManager pm;
    private BaseRender render;

    @InjectEvent(topicId = 1,type = InjectEvent.READ)
    private TopicReader<Vector2i> reader;


    public RenderSystem(PlatformManager pm) {
        super(RenderTrait.class);
        this.pm = pm;
        fps = new OpsCounter("FrameRender");
    }

    @Override
    public void process(IntStream entities) {
        reader.read().forEach(vector -> {});

        entities.forEach(id -> {
            RenderTrait renderTrait = renderMapper.get(id);
            render.render(renderTrait);
        });
        fps.operateAndLog();
        pm.refreshWindow();
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

        try {
            //TODO fake render time
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    void injectEm(EntityManager em) {
        this.renderMapper = em.getMapper(RenderTrait.class);
    }

    public ComponentTask task() {
        return new SystemRTask(Context.MAIN, true, Integer.MAX_VALUE, this);
    }

    public Task prepareTask() {
        return new SystemRWTask(Context.MAIN, false, this) {
            @Override
            public void process() throws Exception {
                LOGGER.info("Preparing");
                pm.attachContext(Context.MAIN);
                StaticShader staticShader = new StaticShader("static.vert", "static.frag");
                render = new BaseRender(staticShader);
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

package com.theopus.client.ecs.system;

import com.google.common.collect.ImmutableMap;
import com.theopus.client.ecs.trait.RenderTrait;
import com.theopus.xengine.conc.SystemRTask;
import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.platform.PlatformManager;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.client.render.Ver0Module;
import com.theopus.xengine.render.opengl.GlRender;
import com.theopus.xengine.render.Render;
import com.theopus.client.render.Ver0ModuleImpl;
import com.theopus.xengine.system.EntitySystem;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.utils.Box;
import com.theopus.xengine.utils.OpsCounter;
import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.BitSet;

public class RenderSystem extends EntitySystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystem.class);
    private final OpsCounter fps;
    private PlatformManager pm;


    private EntityManager em;


    private Render render;

    public RenderSystem(PlatformManager pm) {
        super(RenderTrait.class);
        this.pm = pm;
        fps = new OpsCounter("FrameRender");
    }

    @Override
    public void process(BitSet entities) {
        render.clean();
        render.prepare(em);
        render.render(entities);


        try {
            //TODO fake render time
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fps.operateAndLog();
        pm.refreshWindow();
    }

    @Override
    public void injectEm(EntityManager em) {
        this.em = em;
    }

    public ComponentTask task() {
        return new SystemRTask(Context.MAIN, true, Integer.MAX_VALUE, this);
    }

    public Task prepareTask() {
        return new SystemRWTask(Context.MAIN, false, this) {
            @Override
            public void process() {
                LOGGER.info("Preparing");
                pm.attachContext(Context.MAIN);
                render = new GlRender(ImmutableMap.of(Ver0Module.class, new Ver0ModuleImpl()));
            }
        };
    }


    public Task closeTask() {
        return new SystemRWTask(Context.MAIN, false, this) {

            @Override
            public void process() throws IOException {
                LOGGER.info("Closing RenderTask.");
                render.close();
                pm.detachContext();
            }
        };
    }

    public ComponentTask frameBufferRefresh(TopicReader<Vector2i> reader) {
        return new ComponentTask(Context.MAIN, false, 60, 10) {
            private final TopicReader<Vector2i> _reader = reader;

            {
                components.add(_reader);
            }

            @Override
            public void process() throws Exception {
                Box<Matrix4f> projection = new Box<>();
                _reader.read().forEach(event -> {
                    int w = event.data().x;
                    int h = event.data().y;
                    render.loadViewPort(w, h);
                    projection.set(new Matrix4f().perspective(60, w * 1.0f / h, 0.1f, 1000f));
                });
                if (projection.exist()){
                    render.loadProjection(projection.get());
                }
            }
        };
    }

    public Render getRender() {
        return render;
    }
}

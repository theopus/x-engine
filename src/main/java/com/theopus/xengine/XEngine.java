package com.theopus.xengine;

import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.ecs.EntitySystemManager;
import com.theopus.xengine.event.EventManager;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.Scheduler;
import com.theopus.xengine.platform.PlatformManager;
import com.theopus.xengine.render.Render;
import com.theopus.xengine.utils.TaskUtils;

public class XEngine {
    private final Scheduler scheduler;
    private final EventManager em;
    private final PlatformManager plm;
    private final EntitySystemManager ecm;
    private final Render render;
    private final RateLimiter limiter;

    public XEngine(Scheduler scheduler,
                   EventManager em,
                   PlatformManager plm,
                   EntitySystemManager ecm,
                   Render render) {

        this.scheduler = scheduler;
        this.em = em;
        this.plm = plm;
        this.ecm = ecm;
        this.render = render;
        this.limiter = RateLimiter.create(100_000_000);
    }

    public void run() throws Exception {
        plm.showWindow();
        plm.detachContext();

        while (!plm.shouldClose()) {
            plm.processEvents();
            limiter.acquire();
            scheduler.process();
        }
        scheduler.propose(TaskUtils.teardownCtx(plm, Context.MAIN));
        scheduler.propose(TaskUtils.teardownCtx(plm, Context.SIDE));
        plm.attachContext(Context.MAIN);

        render.close();

        scheduler.close();
        plm.close();
    }
}

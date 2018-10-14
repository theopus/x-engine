package com.theopus.xengine.scheduler;

import com.theopus.xengine.system.System;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntitesTask extends SchedulerTask {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(EntitesTask.class);    

    public EntitesTask(Scheduler.ThreadType threadType, boolean repeatable, System system) {
        super(threadType, repeatable);
        this.system = system;
    }

    @Override
    public void run() {
        this.getSystem().process(this.getState().getEm().entitiesWith(this.getSystem().toPass()).stream());
    }
}

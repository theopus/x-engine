package com.theopus.xengine.scheduler;

import com.theopus.xengine.system.System;

class CallbackTask extends SchedulerTask{

    private final SchedulerTask task;
    private Callback callback;

    public CallbackTask(SchedulerTask task) {
        super(task.getThreadType(), task.isRepeatable());
        this.task = task;
        this.setId(task.getId());
        this.callback = Callback.empty;
    }

    public CallbackTask(SchedulerTask task, Callback callback) {
        super(task.getThreadType(), task.isRepeatable());
        this.task = task;
        this.setId(task.getId());
        this.callback = callback;
    }


    public CallbackTask withCallback(Callback callback){
        this.callback = callback;
        return this;
    }

    @Override
    public System getSystem() {
        return task.getSystem();
    }

    @Override
    public void run() {
        task.run();
        callback.call();
    }
}

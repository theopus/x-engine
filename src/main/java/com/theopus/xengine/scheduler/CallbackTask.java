package com.theopus.xengine.scheduler;

class CallbackTask extends SchedulerTask{

    private final SchedulerTask task;
    private Callback callback;

    public CallbackTask(SchedulerTask task) {
        super(task.getThreadType(), task.isRepeatable());
        this.task = task;
        this.callback = Callback.empty;
    }

    public CallbackTask(SchedulerTask task, Callback callback) {
        super(task.getThreadType(), task.isRepeatable());
        this.task = task;
        this.callback = callback;
    }

    public CallbackTask andAfter(Callback callback){
        this.callback = callback;
        return this;
    }

    @Override
    public void run() {
        task.run();
        callback.call();
    }
}

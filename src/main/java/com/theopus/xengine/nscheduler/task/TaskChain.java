package com.theopus.xengine.nscheduler.task;

public class TaskChain {

    private Task currentTask;
    private Task firstTask;

    public static TaskChain startWith(Task task) {
        TaskChain chain = new TaskChain();
        chain.currentTask = task;
        chain.firstTask = task;
        return chain;
    }

    public TaskChain andThen(Task task) {
        currentTask.onCompete(task);
        currentTask = task;
        return this;
    }

    public TaskChain onFinish(Task task) {
        currentTask.onFinish(task);
        return this;
    }

    public Task head() {
        return firstTask;
    }
}

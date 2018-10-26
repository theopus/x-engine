package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.LockManager;

public class TaskFactory<T> {

    private final LockManager<T> lockManager;
    private final EventManager eventManager;
    private final InputManager inputManager;

    public TaskFactory(LockManager<T> lockManager, EventManager eventManager, InputManager inputManager) {
        this.lockManager = lockManager;
        this.eventManager = eventManager;
        this.inputManager = inputManager;
    }


    public<T extends Task> T injectManagers(T task){
        if (task == null){
            return task;
        }
        task.injectManagers(eventManager, inputManager, lockManager);
        return task;
    }

    public TaskChain injectManagers(TaskChain chain){
        for (Task t = chain.head(); t != null; t = t.getOnComplete()) {
            injectManagers(t);
            injectManagers(t.getOnFinish());
        }
        return chain;
    }
}

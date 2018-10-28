package com.theopus.xengine.conc;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.event.InputData;
import com.theopus.xengine.nscheduler.event.TopicWriter;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.task.Task;

public abstract class EventWriteTask extends Task {

    protected TopicWriter<InputData> writer;

    public EventWriteTask() {
    }

    public EventWriteTask(Context type, boolean cycled) {
        super(type, cycled);
    }

    public EventWriteTask(Context type, boolean cycled, int rate) {
        super(type, cycled, rate);
    }

    public EventWriteTask(Context type, boolean cycled, int rate, int priority) {
        super(type, cycled, rate, priority);
    }

    @Override
    public boolean prepare() {
        return writer.prepare();
    }

    @Override
    public boolean finish() {
        return writer.finish();
    }

    @Override
    public void injectManagers(EventManager em, InputManager im, LockManager lm) {
        writer = em.createWriter(EventManager.Topics.INPUT_DATA_TOPIC);
    }
}

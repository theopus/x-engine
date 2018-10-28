package com.theopus.xengine.conc;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.EventManager;
import com.theopus.xengine.nscheduler.event.InputData;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.input.InputManager;
import com.theopus.xengine.nscheduler.lock.LockManager;
import com.theopus.xengine.nscheduler.task.Task;

public abstract class EventReadTask extends Task {

    protected TopicReader<InputData> reader;

    public EventReadTask() {
    }

    public EventReadTask(Context type, boolean cycled) {
        super(type, cycled);
    }

    public EventReadTask(Context type, boolean cycled, int rate) {
        super(type, cycled, rate);
    }

    public EventReadTask(Context type, boolean cycled, int rate, int priority) {
        super(type, cycled, rate, priority);
    }

    @Override
    public boolean prepare() {
        return reader.prepare();
    }

    @Override
    public boolean finish() {
        return reader.finish();
    }

    //    @Override
//    public void process() throws Exception {
//        reader.read().forEach(System.out::println);
//    }

    @Override
    public void injectManagers(EventManager em, InputManager im, LockManager lm) {
        reader = em.createReader(EventManager.Topics.INPUT_DATA_TOPIC);
    }
}

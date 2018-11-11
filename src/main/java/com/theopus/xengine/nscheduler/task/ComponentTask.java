package com.theopus.xengine.nscheduler.task;

import com.theopus.xengine.nscheduler.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentTask extends Task {

    protected final List<TaskComponent> components = new ArrayList<>();

    public ComponentTask() {
    }

    public ComponentTask(Context type, boolean cycled) {
        super(type, cycled);
    }

    public ComponentTask(Context type, boolean cycled, float rate) {
        super(type, cycled, rate);
    }

    public ComponentTask(Context type, boolean cycled, float rate, int priority) {
        super(type, cycled, rate, priority);
    }

    @Override
    public boolean prepare() {
        for (TaskComponent component : components) {
            boolean prepare = component.prepare();
            if (!prepare) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean rollback() {
        for (TaskComponent component : components) {
            component.rollback();
        }
        return true;
    }

    @Override
    public boolean finish() {
        for (TaskComponent component : components) {
            component.finish();
        }
        return true;
    }

}

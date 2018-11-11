package com.theopus.xengine.nscheduler.task;

public interface TaskComponent {

    boolean prepare();

    boolean rollback();

    boolean finish();
}

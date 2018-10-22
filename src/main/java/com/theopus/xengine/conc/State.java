package com.theopus.xengine.conc;

import com.theopus.xengine.trait.EntityManager;

public class State {

    private EntityManager manager;

    public State(EntityManager manager) {
        this.manager = manager;
    }

    public EntityManager getManager() {
        return manager;
    }
}

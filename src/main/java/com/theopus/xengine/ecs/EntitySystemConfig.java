package com.theopus.xengine.ecs;

import java.util.List;

public class EntitySystemConfig {

    private int shardsNumber;
    private List<Class<? extends Trait>> traits;

    public EntitySystemConfig(int shardsNumber, List<Class<? extends Trait>> traits) {
        this.shardsNumber = shardsNumber;
        this.traits = traits;
    }

    public List<Class<? extends Trait>> traits() {
        return traits;
    }

    public int shardsNumber() {
        return shardsNumber;
    }
}

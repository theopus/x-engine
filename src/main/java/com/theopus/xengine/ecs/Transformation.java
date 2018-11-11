package com.theopus.xengine.ecs;

public interface Transformation<T extends Trait> {

    void apply(TraitsWrapper<T> wrapper);
}

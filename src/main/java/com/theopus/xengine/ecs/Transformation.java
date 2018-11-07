package com.theopus.xengine.ecs;

import com.theopus.xengine.trait.Trait;

public interface Transformation<T extends Trait> {

    void apply(TraitsWrapper<T> wrapper);
}

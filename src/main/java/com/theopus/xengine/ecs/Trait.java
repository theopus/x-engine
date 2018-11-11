package com.theopus.xengine.ecs;

import com.theopus.xengine.ecs.mapper.IDuplicate;

public abstract class Trait<T> implements IDuplicate<T> {

    protected int gen = 0;
}

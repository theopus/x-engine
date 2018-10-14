package com.theopus.xengine.system;

import com.theopus.xengine.trait.Trait;

import java.util.stream.IntStream;

public interface System {

    void process(IntStream entities);

    Configurer configurer();

    Class<? extends Trait>[] toPass();

}

package com.theopus.client.ecs.system;

import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.xengine.ecs.Ecs;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.ecs.system.EntitySystem;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;

import java.util.BitSet;

public class TestSystem extends EntitySystem {

    @Ecs
    private TraitMapper<PositionTrait> render;

    @Inject
    public TestSystem() {
        super(Context.MAIN, true, 0.2f);
    }

    @Override
    public void process(BitSet entities) {
        System.out.println(entities);
    }
}

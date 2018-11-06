package com.theopus.xengine.ecs;

import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.xengine.utils.JsonUtils;
import org.junit.Test;

import java.util.Arrays;

public class EntitySystemManagerTest {

    @Test
    public void name() {
        EntitySystemManager manager = new EntitySystemManager(
                Arrays.asList(PositionTrait.class), 3
        );

        @ReadOnly TraitMapper<PositionTrait> mapper = manager.getReadMapper(PositionTrait.class);

        JsonUtils.prettyPrintJson(mapper);

        mapper.prepare();
        System.out.println(mapper);

        mapper.finish();


    }
}
package com.theopus.xengine.ecs;

import com.theopus.client.ecs.trait.PositionTrait;
import org.junit.Test;

import java.util.Arrays;

public class EntitySystemManagerTest {

    @Test
    public void name() {
        EntitySystemManager manager = new EntitySystemManager(
                Arrays.asList(PositionTrait.class)
        );

        @ReadOnly TraitMapper<PositionTrait> mapper = manager.getReadMapper(PositionTrait.class);


        PositionTrait trait = mapper.get(0);

        @ReadWrite WriteTraitMapper<PositionTrait> wmapper = manager.getWriteMapper(PositionTrait.class);

        @ReadOnly TraitMultiMapper tmanager = manager.getReadManager();
        @ReadWrite WriteTraitMultiMapper wtmanager = manager.getWriteManager();

    }
}
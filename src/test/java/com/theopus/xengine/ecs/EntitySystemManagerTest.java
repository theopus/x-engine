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
        @ReadOnly WriteTraitMapper<PositionTrait> wmapper = manager.getWriteMapper(PositionTrait.class);

        wmapper.prepare();
        JsonUtils.prettyPrintJson(wmapper);
        wmapper.finish();

        wmapper.prepare();
        JsonUtils.prettyPrintJson(wmapper);
        wmapper.finish();

        wmapper.prepare();
        JsonUtils.prettyPrintJson(wmapper);
        wmapper.finish();
        
        wmapper.prepare();
        JsonUtils.prettyPrintJson(wmapper);
        wmapper.finish();

    }
}
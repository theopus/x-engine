package com.theopus.xengine.ecs;

import com.google.common.collect.Lists;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.RenderTrait;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.utils.JsonUtils;
import org.junit.Test;

import java.util.Arrays;

public class EcsProviderTest {

    @Test
    public void name() throws ClassNotFoundException, IllegalAccessException {
        EntitySystemManager manager = new EntitySystemManager(
                Arrays.asList(
                        PositionTrait.class,
                        WorldPositionTrait.class,
                        RenderTrait.class),
                3
        );

        EcsProvider ecsProvider = new EcsProvider(manager);

        Testkek target = new Testkek();
        ecsProvider.provide(target, Lists.newArrayList());
        JsonUtils.prettyPrintJson(target);
    }

    public static class Testkek{
        @Ecs
        TraitMapper<PositionTrait> positionTraits;
    }
}
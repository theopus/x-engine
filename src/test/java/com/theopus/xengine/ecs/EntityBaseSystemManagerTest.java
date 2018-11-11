package com.theopus.xengine.ecs;

import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.RenderTrait;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.ecs.mapper.EntityManager;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.ecs.mapper.WriteTraitMapper;
import com.theopus.xengine.utils.JsonUtils;
import org.junit.Test;

import java.util.Arrays;

public class EntityBaseSystemManagerTest {

    @Test
    public void name() {
        EntitySystemManager manager = new EntitySystemManager(
                Arrays.asList(
                        PositionTrait.class,
                        WorldPositionTrait.class,
                        RenderTrait.class),
                3
        );

        TraitMapper<PositionTrait> mapper = manager.getReadMapper(PositionTrait.class);
        mapper.prepare();
        mapper.get(0);
        mapper.get(1);
        mapper.finish();
        WriteTraitMapper<WorldPositionTrait> wpmapper = manager.getWriteMapper(WorldPositionTrait.class);
        wpmapper.prepare();
        wpmapper.transform(0,wrapper -> System.out.println());
        wpmapper.finish();

        EntityManager em = manager.getEntityManager();
        em.prepare();
        JsonUtils.prettyPrintJson(em);

        em.create(PositionTrait.class);

        em.finish();

        JsonUtils.prettyPrintJson(manager);


    }
}
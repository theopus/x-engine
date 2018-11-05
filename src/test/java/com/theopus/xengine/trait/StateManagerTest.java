package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.PositionTraitEditor;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.client.ecs.trait.WorldPositionTraitEditor;
import org.junit.Test;

public class StateManagerTest {

    @Test
    public void name() {

        EntityManagerFactory factory = new EntityManagerFactory(ImmutableMap.of(
                WorldPositionTrait.class, WorldPositionTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        ));

        EntityManager em = factory.create();

        em.reApplyTransformations();
    }
}

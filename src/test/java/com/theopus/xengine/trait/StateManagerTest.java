package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.custom.RenderTraitEditor;
import org.junit.Test;

public class StateManagerTest {

    @Test
    public void name() {

        EntityManagerFactory factory = new EntityManagerFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        ));

        EntityManager em = factory.create();

        em.reApplyTransformations();
    }
}

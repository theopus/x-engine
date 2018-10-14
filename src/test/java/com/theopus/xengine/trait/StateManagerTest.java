package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.util.BitSet;

public class StateManagerTest {

    @Test
    public void name() {

        EntityManagerFactory factory = new EntityManagerFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        ));

        EntityManager em = factory.create();

        TraitMapper<RenderTrait> rM = em.getManager().getMapper(RenderTrait.class);
        TraitMapper<PositionTrait> pM = em.getManager().getMapper(PositionTrait.class);

        rM.get(0);
        pM.get(0);
        rM.get(1);
        pM.get(1);

        BitSet bitSet = em.entitiesWith(RenderTrait.class);

        System.out.println(bitSet);
    }
}

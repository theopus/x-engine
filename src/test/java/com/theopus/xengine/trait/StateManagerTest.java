package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class StateManagerTest {

    @Test
    public void name() {

        EntityManagerFactory factory = new EntityManagerFactory(ImmutableMap.of(
                RenderTrait.class, RenderTraitEditor.class,
                PositionTrait.class, PositionTraitEditor.class
        ));


        StateManager manager = new StateManager(factory,3);


        State write = manager.forWrite();
        State read = manager.forRead();

        manager.release(write);
        manager.release(read);

        System.out.println(manager.getStates());


    }
}

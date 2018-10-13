package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import org.junit.Test;

public class StateManagerTest {

    @Test
    public void name() {
        StateManager manager = new StateManager(new EntityManager(new TraitManager(
                ImmutableMap.of(
                        RenderTrait.class, RenderTraitEditor.class,
                        PositionTrait.class, PositionTraitEditor.class
                )
        )));
        State state = manager.acquireLastState(StateManager.LockType.READ_ONLY);
        manager.getLastFrame();
        manager.updateState(state);


    }
}

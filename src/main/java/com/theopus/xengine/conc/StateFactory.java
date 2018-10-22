package com.theopus.xengine.conc;

import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockFactory;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.trait.TraitEditor;
import com.theopus.xengine.trait.TraitManager;

import java.util.Map;

public class StateFactory implements LockFactory<State> {

    private final Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> traitsMap;

    public StateFactory(Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> traitsMap) {
        this.traitsMap = traitsMap;
    }

    @Override
    public Lock<State> create(int id) {
        return new StateLock(id, new State(new EntityManager(new TraitManager(traitsMap))));
    }
}

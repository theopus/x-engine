package com.theopus.xengine.conc;

import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.lock.Lock;
import com.theopus.xengine.nscheduler.lock.LockFactory;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.trait.TraitEditor;
import com.theopus.xengine.trait.TraitManager;
import com.theopus.xengine.utils.EcsConfig;

import java.util.Map;

public class StateFactory implements LockFactory<State> {

    private final Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> traitsMap;

    @Inject
    public StateFactory(EcsConfig config) {
        this.traitsMap = config.getMap();
    }

    @Override
    public Lock<State> create(int id) {
        return new StateLock(id, new State(new EntityManager(new TraitManager(traitsMap))));
    }
}

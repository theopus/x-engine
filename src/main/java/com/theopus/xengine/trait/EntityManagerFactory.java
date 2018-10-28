package com.theopus.xengine.trait;

import java.util.Map;

public class EntityManagerFactory {


    private Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> traitsMap;

    public EntityManagerFactory(Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> traitsMap) {
        this.traitsMap = traitsMap;
    }

    public EntityManager create() {
        return new EntityManager(new TraitManager(
                traitsMap
        ));
    }
}

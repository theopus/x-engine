package com.theopus.xengine.utils;

import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.trait.TraitEditor;

import java.util.Map;

public class EcsConfig {

    private Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> map;

    public EcsConfig(Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> map) {

        this.map = map;
    }

    public Map<Class<? extends Trait>, Class<? extends TraitEditor<? extends Trait>>> getMap() {
        return map;
    }
}

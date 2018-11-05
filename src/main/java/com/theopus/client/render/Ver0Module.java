package com.theopus.client.render;

import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.xengine.render.RenderModule;

public interface Ver0Module extends RenderModule<WorldPositionTrait> {
    int load(Ver0Model ver0Model);

}

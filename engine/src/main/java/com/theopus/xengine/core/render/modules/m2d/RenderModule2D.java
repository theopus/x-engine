package com.theopus.xengine.core.render.modules.m2d;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix2D;
import com.theopus.xengine.core.render.ArtemisRenderModule;

public abstract class RenderModule2D<Instance> extends ArtemisRenderModule<String, Instance> {

    @Wire
    ComponentMapper<TransformationMatrix2D> mTransformationMtx;

    @Override
    public String loadToModule(String s) {
        return super.loadToModule(s);
    }

    @Override
    public String loadToModule(String title, String s) {
        return super.loadToModule(title, s);
    }
}

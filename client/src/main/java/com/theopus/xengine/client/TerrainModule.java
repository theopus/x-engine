package com.theopus.xengine.client;

import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;

public abstract class TerrainModule<I> extends ArtemisRenderModule<TerrainData, I> {

    @Wire
    ComponentMapper<Transformation> mTransformation;
    @Wire
    ComponentMapper<TransformationMatrix> mTransformationMtx;

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(Transformation.class, TransformationMatrix.class);
    }

    @Override
    public String loadToModule(TerrainData terrainData) {
        return super.loadToModule(terrainData);
    }
}

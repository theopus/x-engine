package com.theopus.xengine.core.ecs;

import com.artemis.ArchetypeBuilder;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.ecs.components.Transformation;
import com.theopus.xengine.core.ecs.components.Render;
import com.theopus.xengine.core.ecs.components.Velocity;

public class Archetypes {

    public static final ArchetypeBuilder base = new ArchetypeBuilder().add(TransformationMatrix.class, Velocity.class, Transformation.class, Render.class);
}

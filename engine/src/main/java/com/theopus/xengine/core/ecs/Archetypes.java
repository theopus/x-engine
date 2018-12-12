package com.theopus.xengine.core.ecs;

import com.artemis.ArchetypeBuilder;
import com.theopus.xengine.core.ecs.components.ModelMatrix;
import com.theopus.xengine.core.ecs.components.Position;
import com.theopus.xengine.core.ecs.components.Render;
import com.theopus.xengine.core.ecs.components.Velocity;

public class Archetypes {

    public static final ArchetypeBuilder base = new ArchetypeBuilder().add(ModelMatrix.class, Velocity.class, Position.class, Render.class);
}

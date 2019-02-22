package com.theopus.xengine.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.artemis.BaseSystem;
import com.artemis.managers.TagManager;
import com.theopus.xengine.core.ecs.managers.CustomGroupManager;
import com.theopus.xengine.core.ecs.systems.CameraSystem;
import com.theopus.xengine.core.ecs.systems.EventSystem;
import com.theopus.xengine.core.ecs.systems.LightSystem;
import com.theopus.xengine.core.ecs.systems.ModelMatrixSystem;
import com.theopus.xengine.core.ecs.systems.MoveSystem;
import com.theopus.xengine.core.ecs.systems.ProjectionSystem;
import com.theopus.xengine.core.ecs.systems.RenderSystem;
import com.theopus.xengine.core.render.RenderModule;

public class XEngineBuilder {

    Set<Class<? extends RenderModule<?>>> modules = new LinkedHashSet<>();

    Set<Class<? extends BaseSystem>> systems = new LinkedHashSet<>(Arrays.asList(
            TagManager.class,
            EventSystem.class,
            RenderSystem.class,
            CustomGroupManager.class,
            CameraSystem.class,
            ProjectionSystem.class,
            MoveSystem.class,
            ModelMatrixSystem.class,
            LightSystem.class
    ));

    public XEngineBuilder withModule(Class<RenderModule<?>> module){
        modules.add(module);
        return this;
    }

    public XEngineBuilder withSystem(Class<? extends BaseSystem> module){
        systems.add(module);
        return this;
    }

}

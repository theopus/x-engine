package com.theopus.xengine.core.ecs.components;

import com.artemis.Component;
import com.theopus.xengine.core.render.RenderModule;

public class Render extends Component {
    //marker
    public boolean renderable = true;
    public String model;
    public Class<? extends RenderModule> renderer;
}

package com.theopus.xengine.system;

import com.theopus.xengine.trait.*;

public class RenderSystemConfigurer implements Configurer{


    private RenderSystem renderSystem;

    public RenderSystemConfigurer(RenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }

    @Override
    public void setState(State state) {
        renderSystem.setRenderMapper(state.getMapper(RenderTrait.class));
    }
}

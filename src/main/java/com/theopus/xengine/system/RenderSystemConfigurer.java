package com.theopus.xengine.system;

import com.theopus.xengine.trait.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderSystemConfigurer implements Configurer{

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystemConfigurer.class);


    private RenderSystem renderSystem;

    public RenderSystemConfigurer(RenderSystem renderSystem) {
        this.renderSystem = renderSystem;
    }

    @Override
    public StateManager.LockType type() {
        return StateManager.LockType.READ_ONLY;
    }

    @Override
    public void setRead(State state) {
//        LOGGER.info("Render system {}", state.getFrame());

        renderSystem.setRenderMapper(state.getMapper(RenderTrait.class));
    }

    @Override
    public void setWrite(State state) {

    }
}

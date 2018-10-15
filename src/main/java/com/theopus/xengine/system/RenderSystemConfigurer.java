package com.theopus.xengine.system;

import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.State;
import com.theopus.xengine.trait.StateManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RenderSystemConfigurer implements Configurer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RenderSystemConfigurer.class);

    private int lastReadFrame = 0;
    private int diff = 0;

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
        diff = Math.max(state.getFrame() - lastReadFrame, diff);
        LOGGER.debug("Frames draw was {} now {}", lastReadFrame, state.getFrame());
        LOGGER.debug("Render system dif {}", state.getFrame() - lastReadFrame);
        LOGGER.debug("Render system max dif {}", diff);
        lastReadFrame = state.getFrame();

        renderSystem.setRenderMapper(state.getMapper(RenderTrait.class));
    }

    @Override
    public void setWrite(State read, State state) {

    }

}

package com.theopus.xengine.system;

import com.theopus.xengine.trait.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateSystemConfigurer implements Configurer{

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSystemConfigurer.class);

    private UpdateSystem updateSystem;

    public UpdateSystemConfigurer(UpdateSystem updateSystem) {
        this.updateSystem = updateSystem;
    }

    @Override
    public StateManager.LockType type() {
        return StateManager.LockType.READ_WRITE;
    }

    @Override
    public void setRead(State state) {
        LOGGER.debug("Read {}", state.getFrame());

        updateSystem.setRenderMapper(state.getMapper(RenderTrait.class));
        updateSystem.setPositionMapper(state.getMapper(PositionTrait.class));
    }

    @Override
    public void setWrite(State state) {
        LOGGER.debug("Write {}", state.getFrame());
        TraitEditor<RenderTrait> editor = state.getEditor(RenderTrait.class);
        updateSystem.setReditor((RenderTraitEditor) editor);

        TraitEditor<PositionTrait> peditor = state.getEditor(PositionTrait.class);
        updateSystem.setPositionEditor((PositionTraitEditor) peditor);
    }

}

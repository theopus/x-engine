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
        //read
    }

    @Override
    public void setWrite(State read, State write) {

        read.getEm().copyTo(write.getEm());

        updateSystem.setRenderMapper(write.getMapper(RenderTrait.class));
        updateSystem.setPositionMapper(write.getMapper(PositionTrait.class));

        LOGGER.debug("Write {}", write.getTargetFrame());
        TraitEditor<RenderTrait> editor = write.getEditor(RenderTrait.class);
        updateSystem.setReditor((RenderTraitEditor) editor);

        TraitEditor<PositionTrait> peditor = write.getEditor(PositionTrait.class);
        updateSystem.setPositionEditor((PositionTraitEditor) peditor);
    }

}

package com.theopus.xengine.system;

import com.theopus.xengine.trait.State;
import com.theopus.xengine.trait.StateManager;
import com.theopus.xengine.trait.TraitEditor;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputSystemConfigurer implements Configurer{

    private static final Logger LOGGER = LoggerFactory.getLogger(InputSystemConfigurer.class);

    private InputSystem inputSystem;

    public InputSystemConfigurer(InputSystem inputSystem) {
        this.inputSystem = inputSystem;
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


        LOGGER.info("Input {}->{}, on copy from {}", write.getFrame(), write.getTargetFrame(), read.getFrame());

        TraitEditor<PositionTrait> peditor = write.getEditor(PositionTrait.class);

        inputSystem.setPositionMapper(write.getMapper(PositionTrait.class));
        inputSystem.setPositionEditor((PositionTraitEditor) peditor);
    }

}

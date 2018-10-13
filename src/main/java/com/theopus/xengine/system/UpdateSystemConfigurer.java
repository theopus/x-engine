package com.theopus.xengine.system;

import com.theopus.xengine.trait.*;

public class UpdateSystemConfigurer implements Configurer{


    private UpdateSystem updateSystem;

    public UpdateSystemConfigurer(UpdateSystem updateSystem) {
        this.updateSystem = updateSystem;
    }

    @Override
    public void setState(State state) {
        TraitEditor<RenderTrait> editor = state.getEditor(RenderTrait.class);
        TraitEditor<PositionTrait> peditor = state.getEditor(PositionTrait.class);
        updateSystem.setReditor((RenderTraitEditor) editor);
        updateSystem.setPositionEditor((PositionTraitEditor) peditor);

        updateSystem.setRenderMapper(state.getMapper(RenderTrait.class));
        updateSystem.setPositionMapper(state.getMapper(PositionTrait.class));
    }
}

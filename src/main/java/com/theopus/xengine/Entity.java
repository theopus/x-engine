package com.theopus.xengine;

import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.RenderTrait;

public class Entity {

    private RenderTrait renderTrait;
    private PositionTrait positionTrait;

    public Entity(RenderTrait renderTrait, PositionTrait positionTrait) {
        this.renderTrait = renderTrait;
        this.positionTrait = positionTrait;
    }

    public RenderTrait getRenderTrait() {
        return renderTrait;
    }

    public PositionTrait getPositionTrait() {
        return positionTrait;
    }
}

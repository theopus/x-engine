package com.theopus.xengine;

import com.theopus.xengine.trait.PositionTrait;
import com.theopus.xengine.trait.RenderTrait;
import org.joml.Vector3f;

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

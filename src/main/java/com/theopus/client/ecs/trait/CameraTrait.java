package com.theopus.client.ecs.trait;

import com.theopus.xengine.ecs.Trait;

public class CameraTrait extends Trait<CameraTrait> {

    private float distance;
    private int positionTraitId = -1;

    @Override
    public CameraTrait duplicateTo(CameraTrait cameraTrait) {
        cameraTrait.gen = this.gen;
        cameraTrait.distance = this.distance;
        cameraTrait.positionTraitId = this.positionTraitId;
        return cameraTrait;
    }

    public void setPositionReference(int positionTrait) {
        this.positionTraitId = positionTrait;
    }

    public int getPositionTrait() {
        return positionTraitId;
    }
}

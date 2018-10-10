package com.theopus.xengine.system;

import com.theopus.xengine.Entity;
import com.theopus.xengine.Maths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateSystem implements System {

    private List<Entity> entityList = new ArrayList<>();
    private float accelerate;

    public UpdateSystem(Entity ... entity) {
        entityList.addAll(Arrays.asList(entity));
    }

    @Override
    public void process() {
        entityList.forEach(entity -> entity.getPositionTrait().setRotZ(accelerate+=0.00002f));
        entityList.forEach(entity -> {
            Maths.applyTransformations(
                    entity.getPositionTrait().getPosition(),
                    entity.getPositionTrait().getRotX(),
                    entity.getPositionTrait().getRotY(),
                    entity.getPositionTrait().getRotZ(),
                    entity.getPositionTrait().getScale(),
                    entity.getRenderTrait().getTransformation());
        });
    }
}

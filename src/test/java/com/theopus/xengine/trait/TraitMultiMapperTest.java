package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.PositionTraitEditor;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.client.ecs.trait.WorldPositionTraitEditor;
import org.joml.Vector3f;
import org.junit.Test;

public class TraitMultiMapperTest {

    @Test
    public void name() {

        TraitManager traitManager = new TraitManager(
                ImmutableMap.of(
                        WorldPositionTrait.class, WorldPositionTraitEditor.class,
                        PositionTrait.class, PositionTraitEditor.class
                )
        );

        EntityManager em = new EntityManager(traitManager);
        WorldPositionTraitEditor editor = (WorldPositionTraitEditor) em.getTraitManger().getMapper(WorldPositionTrait.class).getEditor();


        int entityId = 0;
        Vector3f position = null;
        float xRot = 0;
        float yRot = 0;
        float zRot = 0;
        float scale = 0;

        editor.transformWith(entityId, position, xRot, yRot, zRot, scale);

    }
}

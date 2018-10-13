package com.theopus.xengine.trait;

import com.google.common.collect.ImmutableMap;
import org.joml.Vector3f;
import org.junit.Test;

public class TraitManagerTest {

    @Test
    public void name() {

        TraitManager traitManager = new TraitManager(
                ImmutableMap.of(
                        RenderTrait.class, RenderTraitEditor.class,
                        PositionTrait.class, PositionTraitEditor.class
                )
        );

        EntityManager em = new EntityManager(traitManager);
        RenderTraitEditor editor = (RenderTraitEditor) em.getManager().getMapper(RenderTrait.class).getEditor();


        int entityId = 0;
        Vector3f position = null;
        float xRot = 0;
        float yRot = 0;
        float zRot = 0;
        float scale = 0;

        editor.transformWith(entityId, position, xRot, yRot, zRot, scale);

    }
}

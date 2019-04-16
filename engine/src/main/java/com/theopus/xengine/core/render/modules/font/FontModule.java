package com.theopus.xengine.core.render.modules.font;

import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.Color;
import com.theopus.xengine.core.ecs.components.Text;
import com.theopus.xengine.core.ecs.components.Transformation2D;
import com.theopus.xengine.core.render.ArtemisRenderModule;

public abstract class FontModule<D> extends ArtemisRenderModule<FontData, D> {
    @Wire
    ComponentMapper<Transformation2D> mTransformation;
    @Wire
    ComponentMapper<Color> mColor;
    Color dColor = new Color();

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(Text.class, Color.class, Transformation2D.class);
    }

    @Override
    public String loadToModule(FontData fontData) {
        return super.loadToModule(fontData);
    }
}

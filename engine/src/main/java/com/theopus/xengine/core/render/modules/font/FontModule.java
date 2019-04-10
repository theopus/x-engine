package com.theopus.xengine.core.render.modules.font;

import com.artemis.ArchetypeBuilder;
import com.theopus.xengine.core.ecs.components.Text;
import com.theopus.xengine.core.render.ArtemisRenderModule;

public abstract class FontModule<D> extends ArtemisRenderModule<FontData, D> {

    @Override
    public ArchetypeBuilder components() {
        return new ArchetypeBuilder().add(Text.class);
    }

    @Override
    public String loadToModule(FontData fontData) {
        return super.loadToModule(fontData);
    }
}

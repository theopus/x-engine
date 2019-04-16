package com.theopus.xengine.core.text;

import org.joml.Vector3f;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.EntityFactory;
import com.theopus.xengine.core.ecs.components.Color;
import com.theopus.xengine.core.ecs.components.GLVaoComponent;
import com.theopus.xengine.core.ecs.components.Text;
import com.theopus.xengine.core.ecs.components.Transformation2D;
import com.theopus.xengine.core.render.modules.font.FontData;
import com.theopus.xengine.core.render.modules.font.FontModule;
import com.theopus.xengine.core.render.modules.font.GlFontModule;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;

public class GLTextManager extends TextManager {

    @Wire
    private GlFontModule module;
    @Wire
    private EntityFactory factory;

    private ComponentMapper<GLVaoComponent> glc;
    private ComponentMapper<Transformation2D> trc;
    private ComponentMapper<Text> tc;
    private ComponentMapper<Color> mColor;

    @Override
    public void loadFont(String fontTitle, String texutreAtlas, String fontFile) {
        module.loadToModule(fontTitle, new FontData(fontTitle, fontFile, texutreAtlas));
    }

    @Override
    public void createText(String text, String font, int size) {
        int textEntity = factory.createFor(FontModule.class);
        TexturedVao textVao = module.createText(text, font, size);

        Color.set(textEntity, mColor, new Vector3f(1f,0.5f,0.5f));
        Text.set(textEntity, tc, text, font);
        GLVaoComponent.set(textEntity, glc, textVao.vao.getId(), textVao.vao.getLength());

        module.bind(font, textEntity);
    }
}

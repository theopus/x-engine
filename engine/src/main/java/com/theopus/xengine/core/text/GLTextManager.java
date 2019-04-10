package com.theopus.xengine.core.text;

import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.GLVaoComponent;
import com.theopus.xengine.core.ecs.components.Text;
import com.theopus.xengine.core.render.modules.font.FontData;
import com.theopus.xengine.core.render.modules.font.GlFontModule;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;

public class GLTextManager extends TextManager {

    @Wire
    private GlFontModule module;

    private ComponentMapper<GLVaoComponent> glc;
    private ComponentMapper<Text> tc;

    @Override
    public void loadFont(String fontTitle, String texutreAtlas, String fontFile) {
        module.loadToModule(fontTitle, new FontData(fontTitle, fontFile, texutreAtlas));
    }

    @Override
    public void createText(String text, String font, int size){
        TexturedVao textVao = module.createText(text, font, size);
        int i = world.create();
        Text textc = tc.create(i);
        textc.body = text;
        textc.font = font;
        GLVaoComponent glVaoComponent = glc.create(i);
        glVaoComponent.vaoId = textVao.vao.getId();
        glVaoComponent.length = textVao.vao.getLength();
        module.bind(font, i);
    }
}

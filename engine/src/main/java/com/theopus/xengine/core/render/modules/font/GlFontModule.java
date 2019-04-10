package com.theopus.xengine.core.render.modules.font;

import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.GLVaoComponent;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.font.TextMeshData;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.FontRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;
import com.theopus.xengine.wrapper.text.Font;

public class GlFontModule extends FontModule<Font> {
    private SimpleLoader loader;

    @Wire
    private GLContext glContext;
    private FontRenderCommand renderCommand;
    @Wire
    ComponentMapper<GLVaoComponent> componentMapper;

    @Override
    public ArchetypeBuilder components() {
        return super.components().add(GLVaoComponent.class);
    }

    public void init() {
        renderCommand = new FontRenderCommand(new StaticShader("text/shader.vert", "text/shader.frag"), glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());
    }

    public TexturedVao createText(String text, String font, float size) {
        Font fnt = get(font);
        TextMeshData string = fnt.createString(text, size);
        TexturedVao texturedVao = loader.loadText(string.getVertexPositions(), string.getTextureCoords(), fnt.getTexture());
        return texturedVao;
    }

    @Override
    public void render(int entityId, Font font) {
        GLVaoComponent glVaoComponent = componentMapper.get(entityId);
        renderCommand.render(glVaoComponent.vaoId, glVaoComponent.length);
    }

    @Override
    public Font load(FontData d) {
        Font font = new Font(d.fontName, d.fontFile, Texture.loadTexture(d.fontAtlas), 1);
        font.load(16d / 9d);
        return font;
    }

    @Override
    public void prepare(Font font) {
        renderCommand.prepare(font.getTexture());
    }

    @Override
    public void finish(Font font) {
        renderCommand.finish();
    }
}

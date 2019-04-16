package com.theopus.xengine.core.render.modules.font;

import org.joml.Vector2f;

import com.artemis.ArchetypeBuilder;
import com.artemis.ComponentMapper;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.Color;
import com.theopus.xengine.core.ecs.components.GLVaoComponent;
import com.theopus.xengine.core.ecs.components.Transformation2D;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.FontRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.Texture;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.FontShader;
import com.theopus.xengine.wrapper.text.Font;
import com.theopus.xengine.wrapper.text.TextMeshData;

public class GlFontModule extends FontModule<Font> {
    private SimpleLoader loader;

    @Wire
    private GLContext glContext;
    private FontRenderCommand renderCommand;
    @Wire
    private ComponentMapper<GLVaoComponent> componentMapper;
    private Transformation2D defaultv = new Transformation2D();

    @Override
    public ArchetypeBuilder components() {
        return super.components().add(GLVaoComponent.class);
    }

    public void init() {
        super.init();
        defaultv.position = new Vector2f(0.5f, 0.5f);
        renderCommand = new FontRenderCommand(new FontShader("text/shader.vert", "text/shader.frag"), glContext.getState());
        loader = new SimpleLoader(glContext.getMemoryContext());
    }

    public TexturedVao createText(String text, String font, float size) {
        Font fnt = get(font);
        TextMeshData string = fnt.createString(text, size);
        TexturedVao texturedVao = loader.loadText(string.vertexes, string.uvs, fnt.getTexture());
        return texturedVao;
    }

    @Override
    public void render(int entityId, Font font) {
        GLVaoComponent glVaoComponent = componentMapper.get(entityId);
        Transformation2D transformation2D = mTransformation.getSafe(entityId, defaultv);
        Color color = mColor.getSafe(entityId, dColor);
        renderCommand.render(glVaoComponent.vaoId, glVaoComponent.length, transformation2D.position, color.rgb);
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

    @Override
    public void prepareModule() {
        renderCommand.prepareCommand();
    }

    @Override
    public void finishModule() {
        renderCommand.finishCommand();
    }
}

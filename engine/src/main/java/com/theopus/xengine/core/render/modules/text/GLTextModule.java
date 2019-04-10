package com.theopus.xengine.core.render.modules.text;

import org.joml.Vector2f;

import com.artemis.ArchetypeBuilder;
import com.artemis.annotations.Wire;
import com.theopus.xengine.core.ecs.components.TransformationMatrix;
import com.theopus.xengine.core.render.ArtemisRenderModule;
import com.theopus.xengine.core.render.GLArtemisRenderModule;
import com.theopus.xengine.core.render.GLContext;
import com.theopus.xengine.wrapper.font.FontType;
import com.theopus.xengine.wrapper.font.GUIText;
import com.theopus.xengine.wrapper.font.TextMeshData;
import com.theopus.xengine.wrapper.opengl.SimpleLoader;
import com.theopus.xengine.wrapper.opengl.commands.TextRenderCommand;
import com.theopus.xengine.wrapper.opengl.objects.TexturedVao;
import com.theopus.xengine.wrapper.opengl.shader.StaticShader;

public class GLTextModule extends TextModule<TexturedVao> {


    private TextRenderCommand textRenderCommand;
    private SimpleLoader loader;

    @Wire
    private GLContext context;

    public void init() {
        textRenderCommand = new TextRenderCommand(new StaticShader("text/shader.vert", "text/shader.frag"), context.getState());
    }

    @Override
    public ArchetypeBuilder components() {
        StaticShader staticShader = new StaticShader("text/shader.vert", "text/shader.frag");
        loader = new SimpleLoader(context.getMemoryContext());
        return new ArchetypeBuilder().add(TransformationMatrix.class);
    }

    @Override
    public void render(int entityId, TexturedVao vao) {
        textRenderCommand.render(vao);
    }

    @Override
    public TexturedVao load(TextData d) {
        FontType fontType = loader.loadFont(d.fontAtlas, d.fontFile);
        GUIText guiText = new GUIText(d.body, 5, fontType, new Vector2f(0,0), 0.5f, false);
        guiText.setColour(1,0,0);
        TextMeshData textMeshData = fontType.loadText(guiText);
        return loader.loadText(textMeshData.getVertexPositions(), textMeshData.getTextureCoords(), fontType.getTextureAtlas());
    }

    @Override
    public void prepare(TexturedVao vao) {
        textRenderCommand.prepare(vao);
    }

    @Override
    public void finish(TexturedVao vao) {
        textRenderCommand.finish();
    }
}

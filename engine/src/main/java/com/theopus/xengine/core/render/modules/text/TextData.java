package com.theopus.xengine.core.render.modules.text;

public class TextData {

    public final String body;
    public final String fontFile;
    public final String fontAtlas;

    public TextData(String body, String fontFile, String fontAtlas) {
        this.body = body;
        this.fontFile = fontFile;
        this.fontAtlas = fontAtlas;
    }
}

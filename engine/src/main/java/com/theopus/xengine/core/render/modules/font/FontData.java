package com.theopus.xengine.core.render.modules.font;

public class FontData {
    public final String fontName;
    public final String fontFile;
    public final String fontAtlas;

    public FontData(String fontName, String fontFile, String fontAtlas) {
        this.fontName = fontName;
        this.fontFile = fontFile;
        this.fontAtlas = fontAtlas;
    }
}

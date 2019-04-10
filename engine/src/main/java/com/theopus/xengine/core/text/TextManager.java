package com.theopus.xengine.core.text;

import com.artemis.Manager;

public abstract class TextManager extends Manager {
   public abstract void loadFont(String fontTitle, String texutreAtlas, String fontFile);

   public abstract void createText(String text, String font);
}

package com.theopus.xengine.wrapper.opengl.objects;

public class MaterialVao {

    public final TexturedVao texturedVao;
    public final Material material;

    public MaterialVao(TexturedVao texturedVao, Material material) {
        this.texturedVao = texturedVao;
        this.material = material;
    }

    @Override
    public String toString() {
        return "MaterialVao{" +
                "texturedVao=" + texturedVao +
                ", material=" + material +
                '}';
    }
}

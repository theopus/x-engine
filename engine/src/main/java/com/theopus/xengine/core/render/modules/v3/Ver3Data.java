package com.theopus.xengine.core.render.modules.v3;

public class Ver3Data {

    public final String obj;
    public final float ambient;
    public final float diffuse;
    public final float specular;
    public final float shininess;

    public Ver3Data(String obj, float ambient, float diffuse, float specular, float shininess) {
        this.obj = obj;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
}

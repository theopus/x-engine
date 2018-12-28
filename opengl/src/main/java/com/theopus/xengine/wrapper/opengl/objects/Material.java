package com.theopus.xengine.wrapper.opengl.objects;

public class Material {
    public final float ambientReflectivity;
    public final float diffuseReflectivity;
    public final float specularReflectivity;
    public final float shininess;

    public Material(float ambientReflectivity, float diffuseReflectivity, float specularReflectivity, float shininess) {
        this.ambientReflectivity = ambientReflectivity;
        this.diffuseReflectivity = diffuseReflectivity;
        this.specularReflectivity = specularReflectivity;
        this.shininess = shininess;
    }

    @Override
    public String toString() {
        return "Material{" +
                "ambientReflectivity=" + ambientReflectivity +
                ", diffuseReflectivity=" + diffuseReflectivity +
                ", specularReflectivity=" + specularReflectivity +
                ", shininess=" + shininess +
                '}';
    }
}

package com.theopus.xengine.opengl.shader;

public final class Uniforms {
    public static final String TRANSFORMATION_MATRIX = "transformationMatrix";
    public static final String PROJECTION_MATRIX = "projectionMatrix";
    public static final String VIEW_MATRIX = "viewMatrix";
    public static final String LIGHT_POSITION = "lightPosition";
    public static final String LIGHT_COLOR = "lightColor";


    public static class Material {
        public static final String VARIABLE = "mat";
        public static final String HAS_TEXTURE = ".hasTexture";
        public static final String REFLECTIVITY = ".reflectivity";
        public static final String SHINE_DAMPER = ".shineDamper";
        public static final String HAS_TRANSPARENCY = ".hasTransparency";
        public static final String USE_FAKE_LIGHT = ".useFakeLight";
    }

    public static class Fog {
        public static final String VARIABLE = "fog";
        public static final String ENABLED = ".enabled";
        public static final String COLOR = ".color";
        public static final String DESITY = ".density";
        public static final String GRADIENT = ".gradient";
    }


    public static class BlendTextures {
        public static final String BLEND_MAP = "blendMapTexture";
        public static final String BACKGROUND = "bgTexture";
        public static final String R = "rTexture";
        public static final String G = "gTexture";
        public static final String B = "bTexture";
    }
}

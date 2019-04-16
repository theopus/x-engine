package com.theopus.xengine.core.ecs.components;

import org.joml.Vector3f;

import com.artemis.Component;
import com.artemis.ComponentMapper;

public class Color extends Component {
    public Vector3f rgb = new Vector3f();

    public static Color set(int entity, ComponentMapper<Color> mColor, Vector3f vector3f) {
        Color color = mColor.get(entity);
        color.rgb.set(vector3f);
        return color;
    }
}

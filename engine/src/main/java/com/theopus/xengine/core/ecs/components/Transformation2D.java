package com.theopus.xengine.core.ecs.components;

import org.joml.Vector2f;

import com.artemis.Component;
import com.artemis.World;

public class Transformation2D extends Component {
    public Vector2f position = new Vector2f();
    public Vector2f rotation = new Vector2f();
    public Vector2f scale = new Vector2f(1,1);

    public static void set(World world, int id, Vector2f position, Vector2f rotation, Vector2f scale) {
        Transformation2D t = world.getMapper(Transformation2D.class).get(id);
        t.position.set(position);
        t.rotation.set(rotation);
        t.scale.set(scale);
    }
}

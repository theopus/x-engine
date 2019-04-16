package com.theopus.xengine.core.ecs.components;

import org.joml.Vector3f;

import com.artemis.Component;

public class Velocity extends Component {
    public Vector3f position = new Vector3f();
    public Vector3f rotation = new Vector3f();
}

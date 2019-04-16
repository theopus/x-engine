package com.theopus.xengine.core.ecs.components;

import org.joml.Matrix4f;

import com.artemis.Component;

public class TransformationMatrix extends Component {
    public Matrix4f model = new Matrix4f().identity();
}

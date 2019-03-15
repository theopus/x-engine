package com.theopus.xengine.core.ecs.components;

import com.artemis.Component;
import org.joml.Matrix4f;

public class TransformationMatrix extends Component {
    public Matrix4f model = new Matrix4f().identity();
}

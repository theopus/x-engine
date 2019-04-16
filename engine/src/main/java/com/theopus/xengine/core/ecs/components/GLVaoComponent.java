package com.theopus.xengine.core.ecs.components;

import com.artemis.ComponentMapper;

public class GLVaoComponent extends GLComponent {
    public int vaoId = -1;
    public int length = -1;

    public static GLVaoComponent set(int id, ComponentMapper<GLVaoComponent> mapper, int vaoId, int length){
        GLVaoComponent c = mapper.get(id);
        c.vaoId = vaoId;
        c.length = length;
        return c;
    }
}

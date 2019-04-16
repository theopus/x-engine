package com.theopus.xengine.core.ecs.components;

import com.artemis.Component;
import com.artemis.ComponentMapper;

public class Text extends Component {
    public String body = "";
    public String font = "";

    public static Text set(int id, ComponentMapper<Text> mapper, String body, String font){
        Text t = mapper.get(id);
        t.body = body;
        t.font = font;
        return t;
    }
}

package com.theopus.xengine.client;

import com.theopus.xengine.core.ModelUtils;
import com.theopus.xengine.core.XEngineBuilder;
import com.theopus.xengine.core.ecs.systems.scipting.JavaExecutionEvent;
import com.theopus.xengine.core.platform.FramebufferEvent;
import com.theopus.xengine.core.render.modules.text.TextData;
import com.theopus.xengine.core.render.modules.text.TextModule;
import com.theopus.xengine.core.render.modules.v0.Ver0Module;
import com.theopus.xengine.core.render.modules.v1.Ver1Module;
import com.theopus.xengine.core.render.modules.v2.Ver2Data;
import com.theopus.xengine.core.render.modules.v2.Ver2Module;
import com.theopus.xengine.core.render.modules.v3.Ver3Data;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;
import com.theopus.xengine.wrapper.font.FontType;
import com.theopus.xengine.wrapper.font.GUIText;
import com.theopus.xengine.wrapper.opengl.objects.Texture;

import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TestClient {

    public static void main(String[] args) throws IOException {
        XEngineBuilder engineBuilder = new XEngineBuilder()
                .withModule(Ver0Module.class)
                .withModule(Ver1Module.class)
                .withModule(Ver2Module.class)
                .withModule(Ver3Module.class)
                .withModule(TextModule.class)
                .withEvent(new JavaExecutionEvent(eec -> {
                    Ver2Module module2 = eec.renderer.get(Ver2Module.class);
                    Ver3Module module3 = eec.renderer.get(Ver3Module.class);
                    TextModule textModule = eec.renderer.get(TextModule.class);

                    module2.load(new Ver2Data("objects/dragon.obj"));
                    module3.load(new Ver3Data("objects/dragon.obj", 0, 1f, 1f, 10f));
                    textModule.load(new TextData("Hi this is test text", "fonts/arial.fnt", "fonts/arial.png"));

                    eec.factory.createCamera();
                    eec.factory.createFor(Ver3Module.class, new Vector3f(0, 0, -5));
                    eec.factory.createFor(TextModule.class, new Vector3f(0, 0, 0));
                    eec.factory.createLight(new Vector3f(1, 1, 1), new Vector3f(0, 200, -5));

                    eec.eventBus.post(new FramebufferEvent(600, 400));

                }));

        engineBuilder.build().run();
    }
}

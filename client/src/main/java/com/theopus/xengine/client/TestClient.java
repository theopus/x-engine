package com.theopus.xengine.client;

import com.theopus.xengine.core.ModelUtils;
import com.theopus.xengine.core.XEngineBuilder;
import com.theopus.xengine.core.ecs.systems.scipting.JavaExecutionEvent;
import com.theopus.xengine.core.platform.FramebufferEvent;
import com.theopus.xengine.core.render.modules.v0.Ver0Module;
import com.theopus.xengine.core.render.modules.v1.Ver1Module;
import com.theopus.xengine.core.render.modules.v2.Ver2Data;
import com.theopus.xengine.core.render.modules.v2.Ver2Module;
import com.theopus.xengine.core.render.modules.v3.Ver3Data;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;
import org.joml.Vector3f;

import java.io.IOException;

public class TestClient {

    public static void main(String[] args) throws IOException {
        XEngineBuilder engineBuilder = new XEngineBuilder()
                .withModule(Ver0Module.class)
                .withModule(Ver1Module.class)
                .withModule(Ver2Module.class)
                .withModule(Ver3Module.class)
                .withEvent(new JavaExecutionEvent(eec -> {
                    Ver0Module module0 = eec.renderer.get(Ver0Module.class);
                    Ver1Module module1 = eec.renderer.get(Ver1Module.class);
                    Ver2Module module2 = eec.renderer.get(Ver2Module.class);
                    Ver3Module module3 = eec.renderer.get(Ver3Module.class);

                    ModelUtils.simpleQuad(module0);
                    ModelUtils.texturedQuad(module1);
                    module2.load(new Ver2Data("objects/dragon.obj"));
                    module3.load(new Ver3Data("objects/dragon.obj", 0, 1f, 1f, 10f));

                    eec.factory.setDefaults(Ver3Module.class);

                    eec.factory.createCamera();
                    eec.factory.createEntity(new Vector3f(0, 0, -5));
                    eec.factory.createLight(new Vector3f(1, 1, 1), new Vector3f(0, 200, -5));

                    eec.eventBus.post(new FramebufferEvent(600, 400));
                }));

        engineBuilder.build().run();
    }
}

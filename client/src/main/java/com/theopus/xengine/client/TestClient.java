package com.theopus.xengine.client;

import java.io.IOException;

import org.joml.Vector3f;

import com.theopus.xengine.core.XEngineBuilder;
import com.theopus.xengine.core.ecs.systems.scipting.JavaExecutionEvent;
import com.theopus.xengine.core.platform.FramebufferEvent;
import com.theopus.xengine.core.render.modules.font.GlFontModule;
import com.theopus.xengine.core.render.modules.v0.GLVer0Module;
import com.theopus.xengine.core.render.modules.v1.GLVer1Module;
import com.theopus.xengine.core.render.modules.v2.GLVer2Module;
import com.theopus.xengine.core.render.modules.v2.Ver2Data;
import com.theopus.xengine.core.render.modules.v2.Ver2Module;
import com.theopus.xengine.core.render.modules.v3.GLVer3Module;
import com.theopus.xengine.core.render.modules.v3.Ver3Data;
import com.theopus.xengine.core.render.modules.v3.Ver3Module;
import com.theopus.xengine.core.text.GLTextManager;

public class TestClient {

    public static void main(String[] args) throws IOException {
        XEngineBuilder engineBuilder = new XEngineBuilder()
                .withModule(GLVer0Module.class)
                .withModule(GLVer1Module.class)
                .withModule(GLVer2Module.class)
                .withModule(GLVer3Module.class)
                .withModule(GlFontModule.class)
                .withModule(GlTerrainModule.class)
                .withSystem(GLTextManager.class)
                .withEvent(new JavaExecutionEvent(eec -> {
                    Ver2Module module2 = eec.renderer.get(Ver2Module.class);
                    Ver3Module module3 = eec.renderer.get(Ver3Module.class);
                    TerrainModule terrainModule = eec.renderer.get(TerrainModule.class);

                    module2.loadToModule(new Ver2Data("objects/dragon.obj"));
                    String s = module3.loadToModule(new Ver3Data("objects/dragon.obj", 0, 1f, 1f, 10f));
                    terrainModule.loadToModule(new TerrainCreator().loadTerrain());
                    GLTextManager textManager = eec.world.getSystem(GLTextManager.class);


                    String texutreAtlas = "fonts/arial.png";
                    String fontFile = "fonts/arial.fnt";
                    String fontTitle = "arial";
                    textManager.loadFont(fontTitle, texutreAtlas, fontFile);
                    textManager.createText("Hi this is test", "arial", 4);

                    int i = eec.factory.createCamera();

                    eec.factory.createFor(Ver3Module.class, new Vector3f(0, 0, -5));
                    eec.factory.createLight(new Vector3f(1, 1, 1), new Vector3f(0, 200, -5));

                    module3.bind(s, i);

                    int terrain0 = eec.factory.createFor(TerrainModule.class, new Vector3f(0,0,0));
                    int terrain1 = eec.factory.createFor(TerrainModule.class, new Vector3f(0,0,-100));
                    int terrain2 = eec.factory.createFor(TerrainModule.class, new Vector3f(-100,0,-100));
                    int terrain3 = eec.factory.createFor(TerrainModule.class, new Vector3f(-100,0,0));

                    eec.eventBus.post(new FramebufferEvent(600, 400));
                }));

        engineBuilder.build().run();
    }
}

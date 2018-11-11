package com.theopus.xengine.render;

import com.google.common.collect.ImmutableMap;
import com.theopus.client.render.Ver0Module;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.platform.GlfwPlatformManager;
import com.theopus.xengine.render.opengl.GlRender;
import com.theopus.client.render.Ver0Model;
import com.theopus.client.render.Ver0ModuleImpl;
import org.junit.Test;

import java.io.IOException;
import java.util.BitSet;

public class GlRenderTest {
    @Test
    public void name() throws IOException {

        GlfwPlatformManager glfwPlatformManager = new GlfwPlatformManager(600, 400);
        glfwPlatformManager.createWindow();
        glfwPlatformManager.attachContext(Context.MAIN);
        glfwPlatformManager.showWindow();

        GlRender render = new GlRender(
                ImmutableMap.of(Ver0Module.class, new Ver0ModuleImpl())
        );


        Ver0Module module = render.module(Ver0Module.class);
        int modelId = module.load(new Ver0Model(new float[]{1f}, new int[]{1}));

        render.bind(1, Ver0Module.class, modelId);

        render.render(1);
        BitSet entities = new BitSet();
        entities.set(1);
        render.render(entities);

        while (!glfwPlatformManager.shouldClose()){
            glfwPlatformManager.processEvents();
            render.clean();
            glfwPlatformManager.refreshWindow();
        }
    }
}
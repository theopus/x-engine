package com.theopus.xengine.core;

import com.theopus.xengine.core.render.RenderModule;
import com.theopus.xengine.core.render.modules.v0.Ver0Data;
import com.theopus.xengine.core.render.modules.v1.Ver1Data;

public class ModelUtils {
    public static String simpleQuad(RenderModule<Ver0Data> module0) {
        return module0.loadToModule(new Ver0Data(
                new float[]{
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,
                },
                new int[]{
                        0, 1, 3,
                        3, 1, 2
                }));
    }

    public static String texturedQuad(RenderModule<Ver1Data> module1) {
        return module1.loadToModule(new Ver1Data(
                new float[]{
                        -0.5f, 0.5f, 0,
                        -0.5f, -0.5f, 0,
                        0.5f, -0.5f, 0,
                        0.5f, 0.5f, 0,
                },
                new float[]{
                        0, 0,
                        0, 1,
                        1, 1,
                        1, 0,
                },
                new int[]{
                        0, 1, 3,
                        3, 1, 2
                },
                "textures/chrome.png"));
    }
}

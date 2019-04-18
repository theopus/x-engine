package com.theopus.xengine.client.cubemap;

import com.theopus.xengine.core.render.ArtemisRenderModule;

public abstract class CubemapModule<Instance> extends ArtemisRenderModule<CubemapData, Instance> {

    @Override
    public String loadToModule(CubemapData cubemapData) {
        return super.loadToModule(cubemapData);
    }

    @Override
    public String loadToModule(String title, CubemapData cubemapData) {
        return super.loadToModule(title, cubemapData);
    }
}

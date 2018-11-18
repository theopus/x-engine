package com.theopus.client.ecs.system;

import com.theopus.client.ecs.trait.CameraTrait;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.RenderTrait;
import com.theopus.xengine.ecs.Ecs;
import com.theopus.xengine.ecs.mapper.TraitMapper;
import com.theopus.xengine.ecs.mapper.ViewEntityManager;
import com.theopus.xengine.ecs.system.EntitySystem;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.platform.PlatformManager;
import com.theopus.xengine.render.Render;
import com.theopus.xengine.utils.CopyUtils;
import com.theopus.xengine.utils.Maths;
import com.theopus.xengine.utils.OpsCounter;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.BitSet;

public class RenderSystem extends EntitySystem {

    private OpsCounter counter = new OpsCounter("Render");

    @Ecs
    private ViewEntityManager manager;
    @Ecs
    private TraitMapper<RenderTrait> mapper;

    private Render render;
    private PlatformManager pm;

    @Inject
    public RenderSystem(Render render, PlatformManager pm) {
        super(Context.MAIN, true, 100_000_000);
        this.render = render;
        this.pm = pm;
    }

    @Override
    public void process(BitSet entities) {

        render.clean();
        render.prepare(manager);
        render.render(entities);
        pm.refreshWindow();

        pm.scanErrors();
        counter.operateAndLog();

        CameraTrait cameraTrait = manager.get(0, CameraTrait.class);
        if (cameraTrait.getPositionTrait() != -1) {
            PositionTrait trait = manager.get(cameraTrait.getPositionTrait(), PositionTrait.class);
            Vector3f translation = new Vector3f();
            CopyUtils.copy(trait.getPosition(), translation);
            translation.x = -translation.x;
            translation.y = -translation.y;
            translation.z = -translation.z;
            Matrix4f viewMtx = Maths.createTransformationMatrix(translation, trait.getRotX(), trait.getRotY(), trait.getRotZ(), 1);
            render.loadView(viewMtx);
        }
    }
}

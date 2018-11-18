package com.theopus.client.ecs.system;

import com.theopus.client.ecs.trait.*;
import com.theopus.client.render.Ver0Model;
import com.theopus.client.render.Ver0Module;
import com.theopus.xengine.ecs.Ecs;
import com.theopus.xengine.ecs.mapper.EntityManager;
import com.theopus.xengine.ecs.system.TaskSystem;
import com.theopus.xengine.inject.Inject;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.render.Render;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class PlaygroundVer0System extends TaskSystem {

    @Ecs
    private EntityManager manager;

    private Render render;

    @Inject
    public PlaygroundVer0System(Render render) {
        super(Context.MAIN, false, 60);
        this.render = render;
    }

    @Override
    public void process() {

        int camera = manager.create();
        manager.transform(camera, CameraTrait.class, wrapper -> {
            CameraTrait cameraTrait = wrapper.get(camera);
            cameraTrait.setPositionReference(1);
        });

        Ver0Module module = render.module(Ver0Module.class);
        int load = module.load(new Ver0Model(
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

        IntStream.range(0, 5).forEach(i -> generate(module, load));


    }


    private void generate(Ver0Module module, int load) {
        int entity = manager.create();
        manager.create(entity, RenderTrait.class);
        manager.transform(entity, PositionTrait.class, w -> w.get(entity).setPosition(new Vector3f(
                ThreadLocalRandom.current().nextFloat(),
                ThreadLocalRandom.current().nextFloat(),
                0)));
        manager.transform(entity, WorldPositionTrait.class, w -> w.get(entity));
        manager.transform(entity, VelocityTrait.class, w -> w.get(entity));
        module.bind(entity, load);

    }

    @Override
    public void init() {

    }
}

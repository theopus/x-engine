package com.theopus.xengine.utils;

import com.theopus.xengine.conc.State;
import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.render.Render;
import com.theopus.client.render.Ver0Module;
import com.theopus.client.render.Ver0Model;
import com.theopus.xengine.system.Configurer;
import com.theopus.client.ecs.system.RenderSystem;
import com.theopus.xengine.system.System;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import org.joml.Vector3f;

public class PlayGround {


    public static ComponentTask ver0(RenderSystem system) {
        System tmpSys = new System() {

            private EntityManager manager;

            @Override
            public void process() {
                Render render = system.getRender();
                Ver0Module module = render.module(Ver0Module.class);

                int modelID = module.load(new Ver0Model(
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

                int e0 = manager.createEntity();
                int e1 = manager.createEntity();
                int e2 = manager.createEntity();
                int e3 = manager.createEntity();

                render.bind(e0, Ver0Module.class, modelID);
                render.bind(e1, Ver0Module.class, modelID);
                render.bind(e2, Ver0Module.class, modelID);
                render.bind(e3, Ver0Module.class, modelID);


                PositionTrait positionTrait0 = manager.getMapper(PositionTrait.class).get(e0);

                PositionTrait positionTrait1 = manager.getMapper(PositionTrait.class).get(e1);
                positionTrait1.setPosition(new Vector3f(-1, -1, 0));
                PositionTrait positionTrait2 = manager.getMapper(PositionTrait.class).get(e2);
                positionTrait2.setPosition(new Vector3f(1, -1, 0));
                PositionTrait positionTrait3 = manager.getMapper(PositionTrait.class).get(e3);
                positionTrait3.setPosition(new Vector3f(1, 1, 0));


                manager.getMapper(WorldPositionTrait.class).get(e0).changed();
                manager.getMapper(WorldPositionTrait.class).get(e1).changed();
                manager.getMapper(WorldPositionTrait.class).get(e2).changed();
                manager.getMapper(WorldPositionTrait.class).get(e3).changed();

                positionTrait0.changed();
                positionTrait1.changed();
                positionTrait2.changed();
                positionTrait3.changed();
            }

            @Override
            public Configurer configurer() {
                return new Configurer() {

                    @Override
                    public void setRead(State state) {

                        manager = state.getManager();
                    }

                    @Override
                    public void setWrite(State read, State write) {
                        EntityManager writeManager = write.getManager();
                        EntityManager readManager = read.getManager();

                        write.getManager().clearEditors();
                        readManager.copyTo(writeManager);
                        manager = writeManager;
                    }
                };
            }

        };

        return new SystemRWTask(Context.MAIN, false, tmpSys) {
            @Override
            public void process() throws Exception {
                system.process();
            }
        };
    }
}

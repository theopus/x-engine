package com.theopus.xengine.utils;

import com.theopus.xengine.conc.State;
import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.opengl.RenderTraitLoader;
import com.theopus.xengine.system.Configurer;
import com.theopus.xengine.system.System;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.RenderTrait;
import org.joml.Vector3f;

public class PlayGround {


    public static ComponentTask ver0(RenderTraitLoader renderTraitLoader) {
        System tmpSys = new System() {

            private EntityManager manager;

            @Override
            public void process() {
                int e = manager.createEntity();
                RenderTrait trait = manager.getMapper(RenderTrait.class).get(e);
                RenderTrait renderTrait = renderTraitLoader.loadEntity(
                        trait,
                        new float[]{
                                -0.5f, 0.5f, 0,
                                -0.5f, -0.5f, 0,
                                0.5f, 0.5f, 0,

                                -0.5f, -0.5f, 0,
                                0.5f, -0.5f, 0,
                                0.5f, 0.5f, 0,

                        }, 6
                );

                RenderTrait renderTrait1 = manager.getMapper(RenderTrait.class).get(1);
                renderTrait.duplicateTo(renderTrait1);
                RenderTrait renderTrait2 = manager.getMapper(RenderTrait.class).get(2);
                renderTrait.duplicateTo(renderTrait2);
                RenderTrait renderTrait3 = manager.getMapper(RenderTrait.class).get(3);
                renderTrait.duplicateTo(renderTrait3);

                PositionTrait positionTrait0 = manager.getMapper(PositionTrait.class).get(0);

                PositionTrait positionTrait1 = manager.getMapper(PositionTrait.class).get(1);
                positionTrait1.setPosition(new Vector3f(-1, -1, 0));
                PositionTrait positionTrait2 = manager.getMapper(PositionTrait.class).get(2);
                positionTrait2.setPosition(new Vector3f(1, -1, 0));
                PositionTrait positionTrait3 = manager.getMapper(PositionTrait.class).get(3);
                positionTrait3.setPosition(new Vector3f(1, 1, 0));
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

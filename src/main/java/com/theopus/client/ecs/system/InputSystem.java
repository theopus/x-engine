package com.theopus.client.ecs.system;

import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.system.EntitySystem;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.PositionTraitEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class InputSystem extends EntitySystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputSystem.class);

    private TraitMapper<PositionTrait> positionMapper;
    private PositionTraitEditor positionEditor;

    @Override
    public void process(BitSet entities) {

    }

    @Override
    public void injectEm(EntityManager em) {
        LOGGER.info("em {}  ", em);
        positionEditor = (PositionTraitEditor) em.getEditor(PositionTrait.class);
    }

    public void handleInput(int key, int action) {
//        LOGGER.info("{} {} ",key, action);

        if (action != 0) {
            positionEditor.rotateSpeed(0, 0.1f);
            positionEditor.rotateSpeed(1, 0.6f);
            positionEditor.rotateSpeed(2, 0.6f);
            positionEditor.rotateSpeed(3, 0.6f);
        } else {
            positionEditor.rotateSpeed(0, 0);
        }

    }


    public Task task(int key, int action) {
        return new SystemRWTask(Context.WORK, false, this) {
            @Override
            public void afterprocess() {
                handleInput(key, action);
            }
        };
    }
}

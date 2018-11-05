package com.theopus.client.ecs.system;

import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.inject.InjectEvent;
import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.InputData;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.task.ComponentTask;
import com.theopus.xengine.system.EntitySystem;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.client.ecs.trait.PositionTrait;
import com.theopus.client.ecs.trait.PositionTraitEditor;
import com.theopus.client.ecs.trait.WorldPositionTrait;
import com.theopus.client.ecs.trait.WorldPositionTraitEditor;
import com.theopus.xengine.utils.OpsCounter;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class UpdateSystem extends EntitySystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSystem.class);
    private final OpsCounter ups;

    private TraitMapper<WorldPositionTrait> rmapper;
    private TraitMapper<PositionTrait> pmapper;
    private WorldPositionTraitEditor reditor;
    private PositionTraitEditor peditor;

    @InjectEvent(topicId = 0, type = InjectEvent.READ)
    private TopicReader<InputData> reader;

    public UpdateSystem() {
        super(WorldPositionTrait.class, PositionTrait.class);
        ups = new OpsCounter("Updates");
    }

    @Override
    public void process(BitSet entities) {

        reader.read().forEach(d->{
            if (d.data().action == GLFW.GLFW_PRESS){
                peditor.rotateSpeed(0, 0.3f);
                peditor.rotateSpeed(1, 0.4f);
                peditor.rotateSpeed(2, 0.4f);
                peditor.rotateSpeed(3, 0.4f);
            } else {
                peditor.rotateSpeed(0, 0f);
            }
        });

        entities.stream().forEach(value -> {

            PositionTrait positionTrait = pmapper.get(value);

            peditor.rotateZ(value, positionTrait.getRotSpeed());
            reditor.transformWith(value, positionTrait.getPosition(),
                    positionTrait.getRotX(),
                    positionTrait.getRotY(),
                    positionTrait.getRotZ(),
                    positionTrait.getScale()
            );
        });
        ups.operateAndLog();
    }

    @Override
    public void injectEm(EntityManager em) {
        pmapper = em.getMapper(PositionTrait.class);
        peditor = (PositionTraitEditor) em.getEditor(PositionTrait.class);

        rmapper = em.getMapper(WorldPositionTrait.class);
        reditor = (WorldPositionTraitEditor) em.getEditor(WorldPositionTrait.class);
    }

    public void setReditor(WorldPositionTraitEditor reditor) {
        this.reditor = reditor;
    }

    public ComponentTask task() {
        return new SystemRWTask(Context.WORK, true, this);
    }
}

package com.theopus.xengine.system;

import com.theopus.xengine.nscheduler.Context;
import com.theopus.xengine.nscheduler.event.Event;
import com.theopus.xengine.nscheduler.event.InputData;
import com.theopus.xengine.nscheduler.event.TopicReader;
import com.theopus.xengine.nscheduler.event.TopicWriter;
import com.theopus.xengine.nscheduler.task.Task;
import com.theopus.xengine.conc.SystemRWTask;
import com.theopus.xengine.trait.*;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import com.theopus.xengine.trait.custom.RenderTrait;
import com.theopus.xengine.trait.custom.RenderTraitEditor;
import com.theopus.xengine.utils.OpsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class UpdateSystem extends EntitySystem {

    private TraitMapper<RenderTrait> rmapper;
    private TraitMapper<PositionTrait> pmapper;
    private RenderTraitEditor reditor;
    private PositionTraitEditor peditor;

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSystem.class);
    private final OpsCounter ups;

    public UpdateSystem() {
        super(RenderTrait.class, PositionTrait.class);
        ups = new OpsCounter("Updates");
    }

    @Override
    public void process(IntStream entities) {

        entities.forEach(value -> {

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
    void injectEm(EntityManager em) {
        pmapper = em.getMapper(PositionTrait.class);
        peditor = (PositionTraitEditor) em.getEditor(PositionTrait.class);

        rmapper = em.getMapper(RenderTrait.class);
        reditor = (RenderTraitEditor) em.getEditor(RenderTrait.class);
    }


    public void setReditor(RenderTraitEditor reditor) {
        this.reditor = reditor;
    }

    private Class[] classes = new Class[]{
            RenderTrait.class, PositionTrait.class
    };

    public Task task() {
        return new SystemRWTask(Context.WORK, true, this);
    }
}

package com.theopus.xengine.system;

import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.trait.*;
import com.theopus.xengine.utils.OpsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpdateSystem implements System {

    private Configurer configurer;
    private TraitMapper<RenderTrait> rmapper;

    private TraitMapper<PositionTrait> pmapper;

    private RenderTraitEditor reditor;

    private PositionTraitEditor peditor;

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSystem.class);
    private final OpsCounter ups;

    public UpdateSystem() {
        configurer = new UpdateSystemConfigurer(this);
        ups = new OpsCounter("Updates");
    }

    @Override
    public void process() {
        PositionTrait positionTrait = pmapper.get(0);
        RenderTrait renderTrait = rmapper.get(0);

        peditor.copy(0, positionTrait);
        reditor.copy(0, renderTrait);


        peditor.rotateZ(0, 1f);
        reditor.transformWith(0, positionTrait.getPosition(),
                positionTrait.getRotX(),
                positionTrait.getRotY(),
                positionTrait.getRotZ(),
                positionTrait.getScale()
        );

        ups.operateAndLog();
    }

    public SchedulerTask task() {
        return new SchedulerTask(Scheduler.ThreadType.ANY, true) {
            @Override
            public void run() {
                try {
                    process();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.setSystem(this);
    }


    public void setReditor(RenderTraitEditor reditor) {
        this.reditor = reditor;
    }

    @Override
    public Configurer configurer() {
        return configurer;
    }

    public void setRenderMapper(TraitMapper<RenderTrait> mapper) {
        this.rmapper = mapper;
    }


    public void setPositionEditor(PositionTraitEditor peditor) {
        this.peditor = peditor;
    }

    public void setPositionMapper(TraitMapper<PositionTrait> pmapper) {
        this.pmapper = pmapper;
    }
}

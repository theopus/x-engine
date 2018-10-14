package com.theopus.xengine.system;

import com.google.common.util.concurrent.RateLimiter;
import com.theopus.xengine.scheduler.EntitesTask;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.trait.*;
import com.theopus.xengine.utils.OpsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

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
    public void process(IntStream entities) {
        PositionTrait positionTrait = pmapper.get(0);

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
        return new EntitesTask(Scheduler.ThreadType.ANY, true, this)
                .setSystem(this)
                .setRateLimiter(RateLimiter.create(60));
    }


    public void setReditor(RenderTraitEditor reditor) {
        this.reditor = reditor;
    }

    @Override
    public Configurer configurer() {
        return configurer;
    }


    Class[] classes = new Class[]{
            RenderTrait.class, PositionTrait.class
    };

    @Override
    public Class<? extends Trait>[] toPass() {
        return classes;
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

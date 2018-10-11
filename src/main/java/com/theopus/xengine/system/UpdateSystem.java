package com.theopus.xengine.system;

import com.theopus.xengine.Entity;
import com.theopus.xengine.WindowManager;
import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.utils.Maths;
import com.theopus.xengine.utils.OpsCounter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateSystem implements System {

    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateSystem.class);

    private final OpsCounter ups;
    private List<Entity> entityList = new ArrayList<>();
    private float accelerate;
    private WindowManager wm;

    public UpdateSystem(WindowManager wm, Entity ... entity) {
        this.wm = wm;
        entityList.addAll(Arrays.asList(entity));
        ups = new OpsCounter("Updates");
    }

    @Override
    public void process() {
        entityList.forEach(entity -> entity.getPositionTrait().setRotZ(accelerate+=0.001f));
        entityList.forEach(entity -> {
            Maths.applyTransformations(
                    entity.getPositionTrait().getPosition(),
                    entity.getPositionTrait().getRotX(),
                    entity.getPositionTrait().getRotY(),
                    entity.getPositionTrait().getRotZ(),
                    entity.getPositionTrait().getScale(),
                    entity.getRenderTrait().getTransformation());
        });
        ups.operateAndLog();
    }

    public SchedulerTask task(){
        return new SchedulerTask(Scheduler.ThreadType.ANY, true) {
            @Override
            public void run() {
                try {
                process();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
    }
}

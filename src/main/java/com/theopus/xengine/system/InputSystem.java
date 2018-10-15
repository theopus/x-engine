package com.theopus.xengine.system;

import com.theopus.xengine.scheduler.Scheduler;
import com.theopus.xengine.scheduler.SchedulerTask;
import com.theopus.xengine.trait.Trait;
import com.theopus.xengine.trait.TraitMapper;
import com.theopus.xengine.trait.custom.PositionTrait;
import com.theopus.xengine.trait.custom.PositionTraitEditor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

public class InputSystem implements System {


    private static final Logger LOGGER = LoggerFactory.getLogger(InputSystem.class);

    private TraitMapper<PositionTrait> positionMapper;
    private PositionTraitEditor positionEditor;
    private InputSystemConfigurer configurer = new InputSystemConfigurer(this);

    @Override
    public void process(IntStream entities) {

    }

    public void handleInput(int key, int action){
//        LOGGER.info("{} {} ",key, action);
        
        if (action != 0) {
            positionEditor.rotateSpeed(0, 0.1f);
        } else {
            positionEditor.rotateSpeed(0, 0);
        }

    }

    @Override
    public Configurer configurer() {
        return configurer;
    }

    @Override
    public Class<? extends Trait>[] toPass() {
        return new Class[]{PositionTrait.class};
    }

    public <T extends Trait> void setPositionMapper(TraitMapper<PositionTrait> positionMapper) {
        this.positionMapper = positionMapper;
    }

    public TraitMapper<PositionTrait> getPositionMapper() {
        return positionMapper;
    }

    public void setPositionEditor(PositionTraitEditor positionEditor) {
        this.positionEditor = positionEditor;
    }

    public PositionTraitEditor getPositionEditor() {
        return positionEditor;
    }

    public SchedulerTask task(int key, int action){
        return new SchedulerTask(Scheduler.ThreadType.WORK, false, this) {
            @Override
            public void run() {
                handleInput(key, action);
            }
        };
    }
}

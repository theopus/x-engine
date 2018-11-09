package com.theopus.xengine.ecs.mapper;

import com.theopus.xengine.ecs.EntitySystemManager;
import com.theopus.xengine.ecs.TraitsWrapper;
import com.theopus.xengine.ecs.Transformation;
import com.theopus.xengine.nscheduler.task.TaskComponent;
import com.theopus.xengine.trait.Trait;

import java.util.BitSet;

public class WriteTraitMapper<T extends Trait> implements TaskComponent {
    private final EntitySystemManager.WrappersPack<T> pack;
    private final Class<T> traitClass;
    private TraitsWrapper<T> readWrapper;
    private TraitsWrapper<T> writeWrapper;

    public WriteTraitMapper(EntitySystemManager entitySystemManager, Class<T> traitClass) {
        this.pack =  entitySystemManager.pack(traitClass);
        this.traitClass = traitClass;
    }

    @Override
    public boolean prepare() {

        this.readWrapper = pack.wrapperForRead();
        if (readWrapper == null){
            return false;
        }
        this.writeWrapper = pack.wrapperForWrite();
        if (writeWrapper == null){
            return false;
        }

        writeWrapper.copyFrom(readWrapper);
        return true;
    }

    @Override
    public boolean rollback() {
        finish();
        return false;
    }

    @Override
    public boolean finish() {
        pack.releaseRead(readWrapper);
        pack.releaseWrite(writeWrapper);
        writeWrapper.flush();
        readWrapper = null;
        writeWrapper = null;
        return true;
    }

    public void transform(int entity, Transformation<T> transformation) {
        writeWrapper.addAndApply(transformation);
    }

    public Class<T> getTraitClass() {
        return traitClass;
    }

    public BitSet bits() {
        return writeWrapper.bits();
    }
}

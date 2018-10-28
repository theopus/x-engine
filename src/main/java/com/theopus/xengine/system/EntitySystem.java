package com.theopus.xengine.system;

import com.theopus.xengine.conc.State;
import com.theopus.xengine.trait.EntityManager;
import com.theopus.xengine.trait.Trait;

import java.util.BitSet;
import java.util.stream.IntStream;

public abstract class EntitySystem implements System {

    private final Configurer configurer = new EsConfigurer();
    private EntityManager em;
    private Class<? extends Trait>[] targets;

    public EntitySystem(Class<? extends Trait>... targets) {
        this.targets = targets;
    }

    @Override
    public void process() {
        injectEm(em);
        BitSet bitSet = em.entitiesWith(targets);
        process(bitSet.stream());
    }

    public abstract void process(IntStream entities);

    abstract void injectEm(EntityManager em);

    @Override
    public Configurer configurer() {
        return configurer;
    }


    private class EsConfigurer implements Configurer {

        @Override
        public void setRead(State state) {

            EntitySystem.this.em = state.getManager();
        }

        @Override
        public void setWrite(State read, State write) {
            EntityManager writeManager = write.getManager();
            EntityManager readManager = read.getManager();

            write.getManager().clearEditors();
            readManager.copyTo(writeManager);
            EntitySystem.this.em = writeManager;
        }
    }
}

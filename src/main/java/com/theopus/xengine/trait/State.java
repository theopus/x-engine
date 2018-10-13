package com.theopus.xengine.trait;

import com.theopus.xengine.system.System;

public class State {

    private final int id;
    private int frame;
    private EntityManager em;

    public State(int id) {
        this.id = id;
    }

    public State(int id, EntityManager em) {
        this.id = id;
        this.em = em;
    }

    public<T extends Trait> TraitEditor<T> getEditor(Class<T> renderTraitClass) {
        TraitMapper<T> mapper = em.getManager().getMapper(renderTraitClass);
        return mapper.getEditor();
    }


    public void attachTo(System system){
        system.configurer().setState(this);
    }

    public<T extends Trait> TraitMapper<T> getMapper(Class<T> renderTraitClass) {
        return em.getManager().getMapper(renderTraitClass);
    }

    public EntityManager getEm() {
        return em;
    }
}

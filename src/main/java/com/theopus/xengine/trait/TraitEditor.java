package com.theopus.xengine.trait;

public class TraitEditor<Trait extends com.theopus.xengine.trait.Trait> {
    protected TraitMapper<Trait> mapper;

    public TraitEditor<Trait> with(TraitMapper<Trait> mapper){
        this.mapper = mapper;
        return this;
    }

}

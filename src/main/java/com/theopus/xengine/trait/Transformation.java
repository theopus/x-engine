package com.theopus.xengine.trait;

public interface Transformation<Trait extends com.theopus.xengine.trait.Trait> {

    void transform(TraitMapper<Trait> mapper);
}

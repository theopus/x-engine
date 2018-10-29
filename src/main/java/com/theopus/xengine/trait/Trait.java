package com.theopus.xengine.trait;

public abstract class Trait<T> implements IDuplicate<T> {
    protected int gen = 0;

    public Trait() {
    }

    public void changed(){
        gen++;
    }
}

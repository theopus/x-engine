package com.theopus.xengine.utils;

public class Box<T> {

    private T t;

    public Box() {
    }

    public T get() {
        return t;
    }

    public void set(T t) {
        this.t = t;
    }

    public boolean exist() {
        return t != null;
    }
}

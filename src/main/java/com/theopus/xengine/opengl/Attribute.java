package com.theopus.xengine.opengl;

public class Attribute {
    private final String name;
    private final int index;
    private final int size;

    public Attribute(String name, int index, int size) {
        this.name = name;
        this.index = index;
        this.size = size;
    }
}

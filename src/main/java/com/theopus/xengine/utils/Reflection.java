package com.theopus.xengine.utils;

public class Reflection {

    public static<T> T newInstance(Class<T> targetClass){
        try {
            return targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

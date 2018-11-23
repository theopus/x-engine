package com.theopus.xengine.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Reflection {

    public static <T> T newInstance(Class<T> targetClass) {
        try {
            return targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T setFinal(Object object, String field, T value) {
        try {
            Field declaredField = object.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredField.setInt(declaredField, declaredField.getModifiers() & ~Modifier.FINAL);
            declaredField.set(object, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException();
        }
        return value;
    }
}

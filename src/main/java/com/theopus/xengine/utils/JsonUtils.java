package com.theopus.xengine.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JsonUtils {

    private static ObjectMapper MAPPER = new ObjectMapper()
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


    public static String toJson(Object o, boolean pretty) {
        try {
            if (pretty) {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(o);
            } else {
                return MAPPER.writeValueAsString(o);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void prettyPrintJson(Object o) {
        System.out.println("" + o.getClass() + "@" + o.hashCode() + "\n" + toJson(o, true));
    }

    public static void printJson(Object o) {
        System.out.println("" + o.getClass() + "@" + o.hashCode() + ":\n" + toJson(o, false));
    }
}

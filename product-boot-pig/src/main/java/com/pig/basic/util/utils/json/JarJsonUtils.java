package com.pig.basic.util.utils.json;

import com.pig.basic.util.utils.ExceptionUtils;
import com.google.gson.Gson;

import java.util.Map;

public class JarJsonUtils {

    private JarJsonUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static <T> T parse(String str, Class<T> cla) {
        Gson gson = new Gson();
        return gson.fromJson(str, cla);
    }

    public static Map<String, Object> parseMap(String str) {
        return parse(str, Map.class);
    }

    public static String toJson(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }


}

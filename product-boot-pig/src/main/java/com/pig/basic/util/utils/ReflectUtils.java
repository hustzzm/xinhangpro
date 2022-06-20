package com.pig.basic.util.utils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    private ReflectUtils() {
        ExceptionUtils.requireNonInstance();
    }


    public static List<Field> getOwnFields(Class<?> cla) {
        List<Field> list = new ArrayList<>();
        for (Field field : cla.getDeclaredFields()) {
            list.add(field);
        }
        return list;
    }

    public static List<Field> getAllFields(Class<?> cla) {
        List<Field> list = new ArrayList<>();
        while (cla != Object.class) {
            list.addAll(getOwnFields(cla));
            cla = cla.getSuperclass();
        }
        return list;
    }

    public static List<Field> getFields(Class<?> cla, boolean isRecursion) {
        if (isRecursion) {
            return getAllFields(cla);
        } else {
            return getOwnFields(cla);
        }
    }


    public static Object invoke(Object obj, String name, Class[] cls, Object[] param) {
        Class cla = obj.getClass();
        try {
            Method method = cla.getMethod(name, cls);
            return method.invoke(obj, param);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getValue(Object o, String name) {
        try {
            Class cla = o.getClass();
            Field field = cla.getField(name);
            field.setAccessible(true);
            return field.get(o);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}

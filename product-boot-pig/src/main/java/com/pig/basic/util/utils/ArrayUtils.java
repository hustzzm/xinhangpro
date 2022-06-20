package com.pig.basic.util.utils;

import java.util.Objects;

public class ArrayUtils {

    private ArrayUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static boolean contain(Object[] os, Object o) {
        Objects.requireNonNull(os);
        for (Object o1 : os) {
            if (Objects.equals(o1, o)) {
                return true;
            }
        }
        return false;
    }


}

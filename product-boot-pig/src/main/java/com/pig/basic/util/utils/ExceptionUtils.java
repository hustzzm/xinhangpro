package com.pig.basic.util.utils;

public class ExceptionUtils {

    private ExceptionUtils() {
        requireNonInstance();
    }

    public static void requireNonInstance() {
        throw new RuntimeException("不允许实例化");
    }

    public static void overflow() {
        throw new RuntimeException("数值溢出");
    }

    public static void crossBorder() {
        throw new RuntimeException("下标越界");
    }

    public static void castError() {
        throw new RuntimeException("不支持该类型转换");
    }

    public static void mustUnique(String name) {
        throw new RuntimeException(name + "必须是惟一的");
    }
    
    public static void impossibility() {
        throw new RuntimeException("不可能发生的");
    }
}

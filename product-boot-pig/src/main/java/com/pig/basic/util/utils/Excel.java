package com.pig.basic.util.utils;


import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Excel {

    String value();

    String format() default "";

    int order() default 1;

}

package com.pig.basic.annotation;

import java.lang.annotation.*;

/**
 * @author linqi
 * 注解在方法上。被注解的方法会在调用时进入AuthorityAspect的解析逻辑
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Authority {
    /**
     * 表名
     * @return
     */
    String tableName() default "";

    /**
     * 模块名
     * @return
     */
    String mode() default "";
}

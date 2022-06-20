package com.pig.basic.annotation;

import java.lang.annotation.*;

/**
 * @author linqi
 * 注解在方法入参上。被标记的参数会被注入createUserId和roleAlias属性
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthUser {
}

package com.pig.basic.annotation;

import java.lang.annotation.*;

/**
 * @author linqi
 * 注解在方法入参上，用于标记修改/删除方法时传入的携带id的那个参数
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthModify {

}

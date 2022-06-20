package com.pig.basic.annotation;

import com.pig.basic.constant.FieldGroup;

import java.lang.annotation.*;

/**
*@discrption:字段校验器(是否必须) 项目中的FieldValidator类一起配合使用
*@user:wengzhongjie
*@createTime:2019-03-17 21:57
*/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Inherited
public @interface Field {
    /**
     * 备注
     * @return
     */
    String comment() default "";

    /**
     * 自定义错误信息
     * @return
     */
    String errorText() default "";

    /**
     * 是否允许引用类型为null
     * @return
     */
    boolean nullable() default true;

    /**
     * 字符串是否允许为空
     * @return
     */
    boolean isEmpty() default true;

    /**
     * 是否必须
     * @return
     */
    boolean required() default false;

    /**
     * 最大字符串的长度
     * @return
     */
    int maxLength() default -1;

    /**
     * 最小字符串的长度
     * @return
     */
    int minLength() default -1;

    /**
     * 最大字符的长度
     * @return
     */
    int maxCharLength() default -1;

    /**
     * 最小字符的长度
     * @return
     */
    int minCharLength() default -1;

    /**
     * 开启特殊字符计算
     * @return
     */
    boolean specialChar() default false;

    /**
     * 是否开启嵌套对象校验
     * @return
     */
    boolean valid() default false;

    /**
     * 是否开启嵌套对象集合校验
     * @return
     */
    boolean validateCollection() default false;

    /**
     * 校验字符串格式
     * @return
     */
    String strFormat() default "";

    /**
     * 区分校验组
     * @return
     */
    FieldGroup fieldGroup() default FieldGroup.DEFAULT;
}

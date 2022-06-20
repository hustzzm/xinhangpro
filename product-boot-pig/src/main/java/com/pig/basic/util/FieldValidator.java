package com.pig.basic.util;

import com.pig.basic.constant.FieldGroup;
import com.pig.basic.exception.BusinessException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @discrption:字段校验器
 * @user:wengzhongjie
 * @createTime:2020-02-05 9:54
 */
public class FieldValidator {


    /**
     * 字段校验方法
     *
     * @param body
     */
    public static void validateFieldAndSuper(Object body) {
        validateFieldAndSuper(body, FieldGroup.DEFAULT);
    }

    public static void validateFieldAndSuper(Object body, FieldGroup fieldGroup) {
        try {
            if (body != null) {
                Class clazz = body.getClass();
                List<Field> fieldList = new ArrayList();
                while (clazz != null) {
                    fieldList.addAll(new ArrayList(Arrays.asList(clazz.getDeclaredFields())));
                    clazz = clazz.getSuperclass();
                }
                Field[] fields = new Field[fieldList.size()];
                fieldList.toArray(fields);
                Field.setAccessible(fields, true);
                foreachValidate(fields, body, fieldGroup);
            }
        } catch (IllegalAccessException e) {
            throw new BusinessException("系统异常:" + e.getMessage());
        }
    }

    private static void foreachValidate(Field[] fields, Object body, FieldGroup fieldGroup) throws IllegalAccessException {
        if (fieldGroup == null) {
            fieldGroup = FieldGroup.DEFAULT;
        }
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(com.pig.basic.annotation.Field.class)) {
                com.pig.basic.annotation.Field fieldAnnotation = field.getAnnotation(com.pig.basic.annotation.Field.class);
                boolean validateFieldGroup = validateFieldGroup(fieldGroup, fieldAnnotation.fieldGroup());
                if (validateFieldGroup) {
                    if (!fieldAnnotation.nullable()) {
                        if (null == field.get(body)) {
                            bssException(fieldAnnotation, fieldAnnotation.comment() + "不能为空");
                        }
                    }
                    if (!fieldAnnotation.isEmpty()) {
                        if (null == field.get(body) || StringUtils.isBlank(field.get(body).toString())) {
                            bssException(fieldAnnotation, fieldAnnotation.comment() + "不能为空");
                        }
                    }
                    int maxLength = fieldAnnotation.maxLength();
                    int minLength = fieldAnnotation.minLength();
                    int maxCharLength = fieldAnnotation.maxCharLength();
                    int minCharLength = fieldAnnotation.minCharLength();
                    boolean sp = fieldAnnotation.specialChar();
                    Object fieldValue = field.get(body);
                    if (fieldValue instanceof String) {
                        String fieldValueStr = String.valueOf(fieldValue);
                        if (maxLength != -1) {
                            if (StringUtils.isNotBlank(fieldValueStr) && fieldValueStr.length() > maxLength) {
                                bssException(fieldAnnotation, fieldAnnotation.comment() + "长度不能大于" + maxLength);
                            }
                        }
                        if (minLength != -1) {
                            if (StringUtils.isNotBlank(fieldValueStr) && fieldValueStr.length() < minLength) {
                                bssException(fieldAnnotation, fieldAnnotation.comment() + "长度不能小于" + minLength);
                            }
                        }
                        int charLength = sp ? DataValidator.getCharlengthAndSpecial(fieldValueStr != null ? fieldValueStr : StringUtils.EMPTY) : DataValidator.getCharlength(fieldValueStr != null ? fieldValueStr : StringUtils.EMPTY);
                        if (maxCharLength != -1) {
                            if (StringUtils.isNotBlank(fieldValueStr) && charLength > maxCharLength) {
                                bssException(fieldAnnotation, fieldAnnotation.comment() + "字符数最多" + maxCharLength);
                            }
                        }
                        if (minCharLength != -1) {
                            if (StringUtils.isNotBlank(fieldValueStr) && charLength < minCharLength) {
                                bssException(fieldAnnotation, fieldAnnotation.comment() + "字符数最少" + minCharLength);
                            }
                        }
                        String dateFormat = fieldAnnotation.strFormat();
                        if (StringUtils.isNotBlank(dateFormat)) {
                            boolean valid = LocalDateUtils.isValid(dateFormat, fieldValueStr);
                            if (!valid) {
                                bssException(fieldAnnotation, fieldAnnotation.comment() + "格式有误,正确:" + fieldAnnotation.strFormat());
                            }
                        }
                    }
                    if (fieldAnnotation.valid()) {
                        validateFieldAndSuper(field.get(body));
                    }

                    if (fieldAnnotation.validateCollection()) {
                        if (Collection.class.isAssignableFrom(field.getType())) {
                            Collection collection = (Collection) field.get(body);
                            if (CollectionUtils.isEmpty(collection)) {
                                bssException(fieldAnnotation, fieldAnnotation.comment() + "不能为空");
                            }
                            Iterator iterator = collection.iterator();
                            while (iterator.hasNext()) {
                                validateFieldAndSuper(iterator.next());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 校验
     *
     * @param fieldGroup
     * @param annotationFieldGroup
     * @return
     */
    private static boolean validateFieldGroup(FieldGroup fieldGroup, FieldGroup annotationFieldGroup) {
        if (fieldGroup != FieldGroup.DEFAULT) {
            if (annotationFieldGroup == FieldGroup.ADD_UPDATE || annotationFieldGroup == fieldGroup) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 默认错误信息与自定义信息输出
     *
     * @param fieldAnnotation
     * @param message
     */
    private static void bssException(com.pig.basic.annotation.Field fieldAnnotation, String message) {
        if (StringUtils.isNotEmpty(fieldAnnotation.errorText())) {
            throw new BusinessException(fieldAnnotation.errorText());
        } else {
            throw new BusinessException(message);
        }
    }

}

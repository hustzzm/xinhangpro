package com.pig.basic.util;

import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MapBeanTransUtil {
    public static <T> T map2Bean(Map<String, Object> map, Class<T> clazz) {
        T obj = null;
        try {
            obj = clazz.getDeclaredConstructor().newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String field = pd.getName();
                if (!map.containsKey(field)) {
                    continue;
                }
                Method writeMethod = pd.getWriteMethod();
                writeMethod.invoke(obj, map.get(field));
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

    public static Map<String, Object> bean2Map(Object obj) {
        Map<String, Object> map = new HashMap<>();
        //获取对象的字节码
        Class<?> clazz = obj.getClass();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                String field = pd.getName();
                Method readMethod = pd.getReadMethod();
                Object value = readMethod.invoke(obj);
                map.put(field, value);
            }
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return map;
    }
}

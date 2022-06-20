package com.pig.basic.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public SpringUtil() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return applicationContext.getBean(name, clazz);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static Object getBean(String name) {
        return applicationContext.getBean(name);
    }

    public static <T> Map<String, T> getBeans(Class<T> clazz) {
        return applicationContext.getBeansOfType(clazz);
    }
}

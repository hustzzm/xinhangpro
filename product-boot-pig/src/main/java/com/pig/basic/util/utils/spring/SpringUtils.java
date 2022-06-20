package com.pig.basic.util.utils.spring;

import com.pig.basic.util.utils.ExceptionUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;


public class SpringUtils {

    private SpringUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static File getResourceFile(String name) {
        Resource resource = new ClassPathResource(name);
        try {
            return resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static InputStream getResourceInputStream(String name) {
        Resource resource = new ClassPathResource(name);
        try {
            return resource.getInputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

package com.pig.basic.util;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gtdev 20200702
 * 判断操作系统
 * 
 */
public class JudgeSystem {

    public static boolean isLinux() {
        return System.getProperty("os.name").toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

    public String JudgeSystem() {
        if (isLinux()) {
            return "linux";
        } else if (isWindows()) {
            return "windows";
        } else {
            return "other system";
        }
    }

//    @Test
    public void fun() {
        boolean flag1 = isLinux();
        // log.info(flag1);
        boolean flag2 = isWindows();
        // System.out.println(flag2);
        // System.out.println(System.getProperty("os.name"));
        String sys = JudgeSystem();
        System.out.println(sys);
    }
}

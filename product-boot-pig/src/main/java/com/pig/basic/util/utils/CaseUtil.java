package com.pig.basic.util.utils;

/**
 * @message:
 * @Author: HYB
 * @Date: 2021/11/22 17:41
 */
public class CaseUtil {

    public String any(String any) {
        if (any.length()>3) {
            if (any.substring(any.length() - 3, any.length()).equals("FU0") ||
                    any.substring(any.length() - 3, any.length()).equals("MU0") ||
                    any.substring(any.length() - 3, any.length()).equals("ZF0") ||
                    any.substring(any.length() - 3, any.length()).equals("QZ0") ||
                    any.substring(any.length() - 3, any.length()).equals("YY0") ||
                    any.substring(any.length() - 3, any.length()).equals("NN0") ||
                    any.substring(any.length() - 3, any.length()).equals("WG0") ||
                    any.substring(any.length() - 3, any.length()).equals("WP0") ||
                    any.substring(any.length() - 3, any.length()).equals("NE0") ||
                    any.substring(any.length() - 3, any.length()).equals("EZ0") ||
                    any.substring(any.length() - 3, any.length()).equals("GG0") ||
                    any.substring(any.length() - 3, any.length()).equals("JJ0") ||
                    any.substring(any.length() - 3, any.length()).equals("DD0") ||
                    any.substring(any.length() - 3, any.length()).equals("MM0") ||
                    any.substring(any.length() - 3, any.length()).equals("QT0")) {
                any = any.substring(0, any.length() - 3);
            }
        }
        return any;
    }
}

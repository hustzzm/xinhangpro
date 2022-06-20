package com.pig.basic.util.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

public class StringUtils {

    private StringUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static final String UTF_8 = "utf-8";
    public static final char UNDERLINE = '_';
    public static final char DOLLAR_SIGN = '$';
    public static final String BLANK = " ";
    public static final String REGEX_BEGIN = "^";
    public static final String REGEX_END = DOLLAR_SIGN + "";

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(Collection<?> col) {
        return col == null || col.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> col) {
        return !isEmpty(col);
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(Object[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(Object[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(byte[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(byte[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(short[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(short[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(int[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(int[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(long[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(long[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(float[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(float[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(double[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(double[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(boolean[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(boolean[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(char[] os) {
        return os == null || os.length == 0;
    }

    public static boolean isNotEmpty(char[] os) {
        return !isEmpty(os);
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return "".equals(obj);
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        return false;
    }

    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isBlank(String str) {
        return isEmpty(str) || ("".equals(str.trim()));
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    public static boolean isBlank(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            return isBlank((String) obj);
        }
        return isEmpty(obj);
    }

    public static boolean isNotBlank(Object obj) {
        return !isBlank(obj);
    }


    public static boolean isUpperCase(char c) {
        return 'A' <= c && c <= 'Z';

    }

    public static boolean isLowerCase(char c) {
        return 'a' <= c && c <= 'z';
    }

    public static char toUpperCase(char c) {
        if (isLowerCase(c)) {
            return (char) (c - 32);
        }
        return c;
    }

    public static char toLowerCase(char c) {
        if (isUpperCase(c)) {
            return (char) (c + 32);
        }
        return c;
    }

    public static boolean isAlphabet(char c) {
        return isUpperCase(c) || isLowerCase(c);
    }

    public static boolean isIdentifier(char c) {
        return isAlphabet(c) || c == DOLLAR_SIGN || c == UNDERLINE;
    }

    public static boolean isNumber(char c) {
        return '0' <= c && c <= '9';
    }

    public static boolean isChinese(char c) {
        return '\u4e00' <= c && c <= '\u9fa5';
    }

    public static String getSuffix(String filename) {
        Objects.requireNonNull(filename);
        int a = filename.lastIndexOf('.');
        if (a == -1) {
            throw new RuntimeException("不是文件名");
        }
        return filename.substring(a + 1);
    }

    public static String removeSuffix(String filename) {
        Objects.requireNonNull(filename);
        int a = filename.lastIndexOf('.');
        if (a == -1) {
            throw new RuntimeException("不是文件名");
        }
        return filename.substring(0, a);
    }

    public static String concatPath(String... paths) {
        Objects.requireNonNull(paths);
        int length = paths.length;
        if (length == 0) {
            throw new RuntimeException("没有需要合并的路径");
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            if (i == length - 1) {
                sb.append(paths[i]);
            } else {
                sb.append(paths[i]);
                sb.append(File.separator);
            }
        }
        return sb.toString();
    }

    public static String concatPathIgnoreEmpty(String... paths) {
        return concatPath(Arrays.stream(paths).filter(s -> isNotBlank(s)).collect(Collectors.toList()).toArray(new String[]{}));
    }

    public static String zero(int size) {
        if (size < 0) {
            throw new RuntimeException("个数不能为负");
        }
        StringBuilder sb = new StringBuilder();
        while (size != 0) {
            sb.append("0");
            size--;
        }
        return sb.toString();
    }


    public static String numberFormat(double d, String s) {
        DecimalFormat format = new DecimalFormat(s);
        return format.format(d);
    }

    public static String numberFormat(long d, String s) {
        DecimalFormat format = new DecimalFormat(s);
        return format.format(d);
    }

    public static String numberFormat(double d, int decimalLength) {
        return numberFormat(d, "#0." + zero(decimalLength));
    }

    public static String numberFormat(long d, int decimalLength) {
        return numberFormat(d, "#0." + zero(decimalLength));
    }

    public static String numberFormat(double d) {
        return numberFormat(d, 2);
    }

    public static String numberFormat(long d) {
        return numberFormat(d, 2);
    }

    public static String base64Encode(String text) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String base64Decode(String text) {
        Base64.Decoder decoder = Base64.getDecoder();
        return new String(decoder.decode(text), StandardCharsets.UTF_8);
    }

    public static String urlEncoder(String text) {

        try {
            return URLEncoder.encode(text, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String urlDecoder(String text) {

        try {
            return URLDecoder.decode(text, UTF_8);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String frontAddZero(String str, int zeroLength) {
        StringBuilder sb = new StringBuilder();
        sb.append(zero(zeroLength));
        sb.append(str);
        return sb.toString();
    }

    public static String rearAddZero(String str, int zeroLength) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(zero(zeroLength));
        return sb.toString();
    }

    public static String removeZeroFront(String str) {
        Objects.requireNonNull(str);
        char[] cs = str.toCharArray();
        int begin = 0;
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == '0') {
                begin++;
            } else {
                break;
            }
        }
        return str.substring(begin);
    }

    public static String removeZeroRear(String str) {
        Objects.requireNonNull(str);
        char[] cs = str.toCharArray();
        int end = cs.length;
        for (int i = cs.length - 1; i > -1; i--) {
            if (cs[i] == '0') {
                end--;
            } else {
                break;
            }
        }
        return str.substring(0, end);
    }

    public static String toBinaryString(int i) {
        String binary = Integer.toBinaryString(i);
        return frontAddZero(binary, 32 - binary.length());
    }

    public static String toBinaryString(long i) {
        String binary = Long.toBinaryString(i);
        return frontAddZero(binary, 64 - binary.length());
    }

    public static String reverse(String str) {
        Objects.requireNonNull(str);
        return new StringBuilder(str).reverse().toString();
    }

    public static String toFirstLower(String s) {
        Objects.requireNonNull(s);
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cs.length; i++) {
            if (i == 0) {
                sb.append(toLowerCase(cs[i]));
            } else {
                sb.append(cs[i]);
            }
        }
        return sb.toString();
    }

    public static String toFirstUpper(String s) {
        Objects.requireNonNull(s);
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cs.length; i++) {
            if (i == 0) {
                sb.append(toUpperCase(cs[i]));
            } else {
                sb.append(cs[i]);
            }
        }
        return sb.toString();
    }

    public static String underlineToHump(String s) {
        Objects.requireNonNull(s);
        String[] ss = s.split(UNDERLINE + "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ss.length; i++) {
            if (i == 0) {
                sb.append(ss[i]);
            } else {
                sb.append(toFirstUpper(ss[i]));
            }
        }
        return sb.toString();
    }

    public static String humpToUnderline(String s) {
        Objects.requireNonNull(s);
        char[] cs = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cs.length; i++) {
            if (isUpperCase(cs[i])) {
                sb.append(UNDERLINE);
                sb.append(toLowerCase(cs[i]));
            } else {
                sb.append(cs[i]);
            }
        }
        return sb.toString();
    }

    public static String getStandardRegex(String regex) {
        return REGEX_BEGIN + regex + REGEX_END;
    }

    public static String returnDefault(String str, String defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        return str;
    }


    /**
     * 拼接定义文件路径  /多余可去除
     *
     * @param paramValue
     * @param secondDir
     * @return
     */
    public static String concatFilePath(String paramValue, String secondDir) {
        if (!isBlank(paramValue)) {
            boolean equals = File.separator.equals(paramValue.substring(paramValue.length() - 1));
            return paramValue + (equals ? "" : File.separator) + secondDir;
        }
        return secondDir;
    }

    public static String concatFilePath(boolean prefixSeparator,String paramValue, String secondDir){
        String path = concatFilePath(paramValue, secondDir);
        if(prefixSeparator && !File.separator.equals(String.valueOf(path.charAt(0)))){
            path=File.separator+path;
        }
        return path;
    }

    /**
     * 获取map的值
     * @param map
     * @param key
     * @return
     */
    public static String getVal(Map<String, Object> map, String key){
        Object o = map.get(key);
        if(o == null){
            return "";
        }
        return String.valueOf(o);
    }
}

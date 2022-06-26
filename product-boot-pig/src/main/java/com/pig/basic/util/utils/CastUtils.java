package com.pig.basic.util.utils;


import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CastUtils {

    private CastUtils() {
        ExceptionUtils.requireNonInstance();
    }

    public static final String UTF_8 = StringUtils.UTF_8;

    public static <T> Map<String, Object> entityToMap(T t, List<Field> fields) {
        if (t == null) {
            return null;
        }
        try {
            Map<String, Object> map = new HashMap<>();
            for (Field f : fields) {
                f.setAccessible(true);
                map.put(f.getName(), f.get(t));
            }
            return map;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Map<String, Object> entityToMap(T t, boolean isRecursion) {
        if (t == null) {
            return null;
        }
        Class<?> cla = t.getClass();
        List<Field> fields = ReflectUtils.getFields(cla, isRecursion);
        return entityToMap(t, fields);
    }

    public static <T> List<T> toList(Set<T> set) {
        List<T> list = new ArrayList<>();
        for (T t : set) {
            list.add(t);
        }
        return list;
    }

    public static <T> List<T> toList(T... ts) {
        List<T> list = new ArrayList<>();
        for (T t : ts) {
            list.add(t);
        }
        return list;
    }

    public static List<Object> toList() {
        return new ArrayList<>();
    }

    public static List<Byte> toList(byte... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Short> toList(short... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Integer> toList(int... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Long> toList(long... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Float> toList(float... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Double> toList(double... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Character> toList(char... ts) {
        return (List) toList((Object) ts);
    }

    public static List<Boolean> toList(boolean... ts) {
        return (List) toList((Object) ts);
    }

    private static List<Object> toList(Object ts) {
        if (ts.getClass().isArray()) {
            List<Object> list = new ArrayList<>();
            int length = Array.getLength(ts);
            for (int i = 0; i < length; i++) {
                list.add(Array.get(ts, i));
            }
            return list;
        }
        ExceptionUtils.castError();
        return null;
    }

    public static <T> Set<T> toSet(List<T> list) {
        Set<T> set = new HashSet<>();
        for (T t : list) {
            set.add(t);
        }
        return set;
    }

    public static <T> Set<T> toSet(T... ts) {
        Set<T> set = new HashSet<>();
        for (T t : ts) {
            set.add(t);
        }
        return set;
    }

    public static Set<Object> toSet() {
        return new HashSet<>();
    }

    public static Set<Byte> toSet(byte... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Short> toSet(short... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Integer> toSet(int... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Long> toSet(long... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Float> toSet(float... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Double> toSet(double... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Character> toSet(char... ts) {
        return (Set) toSet((Object) ts);
    }

    public static Set<Boolean> toSet(boolean... ts) {
        return (Set) toSet((Object) ts);
    }

    private static Set<Object> toSet(Object ts) {
        if (ts.getClass().isArray()) {
            Set<Object> set = new HashSet<>();
            int length = Array.getLength(ts);
            for (int i = 0; i < length; i++) {
                set.add(Array.get(ts, i));
            }
            return set;
        }
        ExceptionUtils.castError();
        return null;
    }

    public static Object[] toArray(Collection<?> collection) {

        return collection.toArray();
    }

    public static File toFile(String path) {
        return new File(path);
    }

    public static File toFile(Path path) {
        return path.toFile();
    }

    public static File[] toFiles(String[] paths) {
        int length = paths.length;
        File[] files = new File[length];
        for (int i = 0; i < length; i++) {
            files[i] = toFile(paths[i]);
        }
        return files;
    }

    public static File[] toFiles(Path[] paths) {
        int length = paths.length;
        File[] files = new File[length];
        for (int i = 0; i < length; i++) {
            files[i] = toFile(paths[i]);
        }
        return files;
    }

    public static Path toPath(String path) {
        return Paths.get(path);
    }

    public static Path toPath(File file) {
        return file.toPath();
    }


    public static Path[] toPaths(String[] paths) {
        int length = paths.length;
        Path[] ps = new Path[length];
        for (int i = 0; i < length; i++) {
            ps[i] = toPath(paths[i]);
        }
        return ps;
    }

    public static Path[] toPaths(File[] files) {
        int length = files.length;
        Path[] ps = new Path[length];
        for (int i = 0; i < length; i++) {
            ps[i] = toPath(files[i]);
        }
        return ps;
    }

    public static String toString(InputStream is, String charset) throws IOException {
        Objects.requireNonNull(is);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        FileUtils.readFile(os, is);
        return os.toString(charset);
    }

    public static String toString(InputStream is) throws IOException {
        return toString(is, UTF_8);
    }

    public static String toString(byte[] bytes, String charset) {
        return new String(bytes, Charset.forName(charset));
    }

    public static String toString(byte[] bytes) {
        return toString(bytes, UTF_8);
    }

    public static byte[] toBytes(String str, String charset) {
        return str.getBytes(Charset.forName(charset));
    }

    public static byte[] toBytes(String str) {
        return toBytes(str, UTF_8);
    }

//    public static byte[] toBytes(InputStream is) {
//        try {
//            return is.readAllBytes();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static byte[] toBytes(ByteArrayOutputStream os) {

        return os.toByteArray();
    }

    public static ByteArrayInputStream toByteArrayInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

    public static ByteArrayInputStream toByteArrayInputStream(String str) {
        return new ByteArrayInputStream(toBytes(str));
    }

}

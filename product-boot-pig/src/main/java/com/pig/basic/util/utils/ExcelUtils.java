package com.pig.basic.util.utils;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public abstract class ExcelUtils {

    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME_MILLI = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String TWO_DECIMAL = "#,##0.00";
    public static final String CNY = "￥" + TWO_DECIMAL;
    public static final String USD = "$" + TWO_DECIMAL;
    public static final String PERCENTAGE_TWO = TWO_DECIMAL + "%";
    public static final String VERIFY_REQUIRED_SYMBOL = "*";
    public static final String BACK_QUOTE = "`";
    public static final String SINGLE_QUOTE = "'";


    //row的空行跳过，cell的空行占位
    public static List<List<Object>> parseExcel(Workbook workbook, int sheetIndex) {
        List<List<Object>> list = new ArrayList<>();
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            List<Object> l = new ArrayList<>();
            int cellNum = row.getLastCellNum();
            for (int j = 0; j < cellNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    l.add("");
                } else {
                    CellType cellType = cell.getCellType();
                    switch (cellType) {
                        case ERROR:
                        case _NONE:
                        case BLANK:
                            l.add("");
                            break;
                        case STRING:
                            l.add(cell.getStringCellValue());
                            break;
                        case BOOLEAN:
                            l.add(cell.getBooleanCellValue());
                            break;
                        case FORMULA:
                            l.add(cell.getCellFormula());
                            break;
                        case NUMERIC:
                            if (DateUtil.isCellDateFormatted(cell)) {
                                l.add(cell.getLocalDateTimeCellValue());
                            } else {
                                double d = cell.getNumericCellValue();
                                if (isInt(d)) {
                                    l.add((int) d);
                                } else {
                                    l.add(d);
                                }
                            }
                    }
                }
            }
            list.add(l);
        }
        return list;
    }

    public static boolean isInt(double d) {
        return d == (int) d;
    }

    public static Workbook createWorkbook(InputStream is, String suffix) throws IOException {
        if ("xls".equals(suffix)) {
            return new HSSFWorkbook(is);
        } else if ("xlsx".equals(suffix)) {
            return new XSSFWorkbook(is);
        } else {
            throw new RuntimeException("不支持该拓展名");
        }
    }

    public static Workbook createWorkbook(String suffix) {
        if ("xls".equals(suffix)) {
            return new HSSFWorkbook();
        } else if ("xlsx".equals(suffix)) {
            return new XSSFWorkbook();
        } else {
            throw new RuntimeException("不支持该拓展名");
        }
    }

    public static List<List<Object>> parseExcel(String path, int sheetIndex) {
        try (InputStream is = Files.newInputStream(Paths.get(path));
             Workbook wk = createWorkbook(is, StringUtils.getSuffix(path))) {
            return parseExcel(wk, sheetIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<List<Object>> parseExcel(InputStream is, String name, int sheetIndex) {
        try (Workbook wk = createWorkbook(is, StringUtils.getSuffix(name))) {
            return parseExcel(wk, sheetIndex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<Map<String, Object>> parseExcel(List<List<Object>> list, int titleRow) {
        List<Map<String, Object>> l = new ArrayList<>();
        List<Object> title = list.get(titleRow);
        for (int i = titleRow + 1; i < list.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            for (int j = 0; j < title.size(); j++) {
                List<Object> row = list.get(i);
                if (j < row.size()) {
                    map.put((String) title.get(j), row.get(j));
                } else {
                    map.put((String) title.get(j), null);
                }
            }
            l.add(map);
        }
        return l;
    }

    public static List<Map<String, Object>> parseExcel(String path, int sheetIndex, int titleRow) {
        List<List<Object>> list = parseExcel(path, sheetIndex);
        return parseExcel(list, titleRow);
    }

    public static List<Map<String, Object>> parseExcel(InputStream is, String name, int sheetIndex, int titleRow) {
        List<List<Object>> list = parseExcel(is, name, sheetIndex);
        return parseExcel(list, titleRow);
    }

    public static List<Map<String, Object>> parseExcel(String path) {
        return parseExcel(path, 0, 0);
    }

    public static List<Map<String, Object>> parseExcel(InputStream is, String name) {
        return parseExcel(is, name, 0, 0);
    }


    public static void exportExcel(List<Map<String, Object>> list, OutputStream os, Map<String, String> format, String suffix) {
        if (list == null || list.isEmpty()) {
            return;
        }
        try (Workbook workbook = createWorkbook(suffix)) {
            Sheet sheet = workbook.createSheet();
            Map<String, Object> map = list.get(0);
            List<String> keys = map.keySet().stream().collect(Collectors.toList());
            Row row = sheet.createRow(0);
            for (int i = 0; i < keys.size(); i++) {
                Cell cell = row.createCell(i);
                cell.setCellValue(keys.get(i));
            }
            short date = workbook.createDataFormat().getFormat(DATE_TIME);
            short decimal = workbook.createDataFormat().getFormat(TWO_DECIMAL);
            Short[] styles = new Short[keys.size()];
            if (format != null && !format.isEmpty()) {
                for (int i = 0; i < keys.size(); i++) {
                    String k = keys.get(i);
                    String style = format.get(k);
                    if (StringUtils.isNotEmpty(style)) {
                        styles[i] = workbook.createDataFormat().getFormat(style);
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {
                Map<String, Object> m = list.get(i);
                Row r = sheet.createRow(i + 1);
                for (int j = 0; j < keys.size(); j++) {
                    Cell cell = r.createCell(j);
                    String k = keys.get(j);
                    Object v = m.get(k);
                    if (v instanceof Byte) {
                        cell.setCellValue((Byte) v);
                    } else if (v instanceof Short) {
                        cell.setCellValue((Short) v);
                    } else if (v instanceof Integer) {
                        cell.setCellValue((Integer) v);
                    } else if (v instanceof Long) {
                        cell.setCellValue((Long) v);
                    } else if (v instanceof BigInteger) {
                        cell.setCellValue(((BigInteger) v).doubleValue());
                    } else if (v instanceof Boolean) {
                        cell.setCellValue((Boolean) v);
                    } else if (v instanceof Float || v instanceof Double || v instanceof BigDecimal) {
                        if (v instanceof Float) {
                            cell.setCellValue((Float) v);
                        } else if (v instanceof Double) {
                            cell.setCellValue((Double) v);
                        } else {
                            cell.setCellValue(((BigDecimal) v).doubleValue());
                        }
                        CellStyle cellStyle = workbook.createCellStyle();
                        cell.setCellStyle(cellStyle);
                        if (styles[j] == null) {
                            cellStyle.setDataFormat(decimal);
                        } else {
                            cellStyle.setDataFormat(styles[j]);
                        }
                    } else if (v instanceof Date || v instanceof LocalDate || v instanceof LocalDateTime) {
                        if (v instanceof Date) {
                            cell.setCellValue((Date) v);
                        } else if (v instanceof LocalDate) {
                            cell.setCellValue((LocalDate) v);
                        } else {
                            cell.setCellValue((LocalDateTime) v);
                        }
                        CellStyle cellStyle = workbook.createCellStyle();
                        cell.setCellStyle(cellStyle);
                        if (styles[j] == null) {
                            cellStyle.setDataFormat(date);
                        } else {
                            cellStyle.setDataFormat(styles[j]);
                        }
                    } else if (v == null) {
                        cell.setCellValue("");
                    } else {
                        cell.setCellValue(v.toString());
                    }
                }
            }
            workbook.write(os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void exportExcel(List<Map<String, Object>> list, OutputStream os, String suffix) {
        exportExcel(list, os, null, suffix);
    }

    public static void exportExcel(List<Map<String, Object>> list, String path) {
        try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
            exportExcel(list, os, StringUtils.getSuffix(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void exportExcel(List<Map<String, Object>> list, String path, Map<String, String> format) {
        try (OutputStream os = Files.newOutputStream(Paths.get(path))) {
            exportExcel(list, os, format, StringUtils.getSuffix(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void exportExcel2(List<T> list, String path) {
        List<Map<String, Object>> l = new ArrayList<>();
        Map<String, String> format = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return;
        }
        T t0 = list.get(0);
        Field[] fields = t0.getClass().getDeclaredFields();
        List<Field> fs = Arrays.stream(fields).filter(f -> f.getAnnotation(Excel.class) != null)
                .sorted(Comparator.comparingInt(a -> a.getAnnotation(Excel.class).order())).collect(Collectors.toList());
        for (Field f : fs) {
            f.setAccessible(true);
            Excel excel = f.getAnnotation(Excel.class);
            format.put(excel.value(), excel.format());//f.getName()
        }
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            Map<String, Object> map = new LinkedHashMap<>();
            for (Field f : fs) {
                try {
                    map.put(f.getAnnotation(Excel.class).value(), f.get(t));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            l.add(map);
        }
        exportExcel(l, path, format);
    }

    public static <T> void exportExcel2(List<T> list, OutputStream os, String suffix) {
        List<Map<String, Object>> l = new ArrayList<>();
        Map<String, String> format = new HashMap<>();
        if (list == null || list.isEmpty()) {
            return;
        }
        T t0 = list.get(0);
        Field[] fields = t0.getClass().getDeclaredFields();
        List<Field> fs = Arrays.stream(fields).filter(f -> f.getAnnotation(Excel.class) != null)
                .sorted(Comparator.comparingInt(a -> a.getAnnotation(Excel.class).order())).collect(Collectors.toList());
        for (Field f : fs) {
            f.setAccessible(true);
            Excel excel = f.getAnnotation(Excel.class);
            format.put(excel.value(), excel.format());//f.getName()
        }
        for (int i = 0; i < list.size(); i++) {
            T t = list.get(i);
            Map<String, Object> map = new LinkedHashMap<>();
            for (Field f : fs) {
                try {
                    map.put(f.getAnnotation(Excel.class).value(), f.get(t));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
            l.add(map);
        }
        exportExcel(l, os, format, suffix);
    }

    public static String verify(List<Map<String, Object>> list) {
        if (list == null || list.isEmpty()) {
            return "没有要导入的数据";
        }
        Set<String> keys = list.get(0).keySet();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map = list.get(i);
            for (String key : keys) {
                if (key.startsWith(VERIFY_REQUIRED_SYMBOL)) {
                    Object v = map.get(key);
                    if (StringUtils.isBlank(v)) {
                        return "第" + (i + 2) + "行数据的" + key + "这列，内容是空白的";
                    }
                }
            }
        }
        return null;
    }

    public static List<Map<String, Object>> standardizing(List<Map<String, Object>> list, Map<String, String> map, Map<String, Map<Object, Object>> dict) {
        List<Map<String, Object>> l = new ArrayList<>();
        Set<String> keys = list.get(0).keySet();
        list.forEach(m -> {
            Map<String, Object> m2 = new HashMap<>();
            for (String key : keys) {
                Object v = m.get(key);
                if (key.startsWith(VERIFY_REQUIRED_SYMBOL)) {
                    key = key.replace(VERIFY_REQUIRED_SYMBOL, "");
                }
                if (StringUtils.isNotEmpty(map)) {
                    String nk = map.get(key);
                    if (StringUtils.isNotBlank(nk)) {
                        key = nk;
                    }
                }
                if (v instanceof String) {
                    v = ((String) v).trim();
                }
                if (StringUtils.isNotEmpty(dict)) {
                    Map<Object, Object> m3 = dict.get(key);
                    if (StringUtils.isNotEmpty(m3)) {
                        Object o = m3.get(v);
                        if (o != null) {
                            v = o;
                        }
                    }
                }
                m2.put(key, v);
            }
            l.add(m2);
        });
        return l;
    }


    private static String getNameSql(String name) {

        return BACK_QUOTE + name + BACK_QUOTE;
    }

    public static String getConstantSql(String name) {

        return SINGLE_QUOTE + name + SINGLE_QUOTE;
    }

    public static Object[] getInsertSql(Map<String, Object> map, String tableName) {
        return getInsertSql(map, tableName, (List) null);
    }

    public static Object[] getInsertSql(Map<String, Object> map, String tableName, String... exclude) {
        return getInsertSql(map, tableName, Arrays.stream(exclude).collect(Collectors.toList()));
    }

    public static Object[] getInsertSql(Map<String, Object> map, String tableName, List<String> exclude) {
        Set<String> keys = map.keySet();
        List<String> ks = new ArrayList<>();
        List<Object> os = new ArrayList<>();
        for (String key : keys) {
            Object value = map.get(key);
            if (StringUtils.isEmpty(exclude)) {
                if (StringUtils.isNotBlank(value)) {
                    ks.add(key);
                    os.add(value);
                }
            } else {
                if (StringUtils.isNotBlank(value) && !exclude.contains(key)) {
                    ks.add(key);
                    os.add(value);
                }
            }
        }
        Object[] objs = new Object[2];
        objs[0] = getInsertSql(ks, tableName);
        objs[1] = os.toArray();
        return objs;
    }

    public static Object[] getUpdateSql(Map<String, Object> map, String tableName, String[] by, String... exclude) {
        return getUpdateSql(map, tableName, Arrays.stream(by).collect(Collectors.toList()), Arrays.stream(exclude).collect(Collectors.toList()));
    }

    public static Object[] getUpdateSql(Map<String, Object> map, String tableName, List<String> by, List<String> exclude) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ");
        sb.append(getNameSql(tableName));
        sb.append(" set ");
        Set<String> keys = map.keySet();
        if (!keys.containsAll(by)) {
            throw new RuntimeException("不符合规范");
        }
        List<String> ks = new ArrayList<>();
        List<Object> os = new ArrayList<>();
        List<Object> os2 = new ArrayList<>();
        for (String key : keys) {
            Object value = map.get(key);
            if (StringUtils.isEmpty(exclude)) {
                if (StringUtils.isNotBlank(value) && !by.contains(key)) {
                    ks.add(key);
                    os.add(value);
                }
            } else {
                if (StringUtils.isNotBlank(value) && !exclude.contains(key) && !by.contains(key)) {
                    ks.add(key);
                    os.add(value);
                }
            }
        }
        for (String key : by) {
            os2.add(map.get(key));
        }
        os.addAll(os2);
        sb.append(ks.stream().map(s -> getNameSql(s) + "=? ").collect(Collectors.joining(",")));
        sb.append(" where ");
        sb.append(by.stream().map(t -> getNameSql(t) + "=?").collect(Collectors.joining(" and ")));
        Object[] objs = new Object[2];
        objs[0] = sb.toString();
        objs[1] = os.toArray();
        return objs;
    }

    public static String getInsertSql(List<String> keys, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" insert into ");
        sb.append(getNameSql(tableName));
        sb.append(keys.stream().map(s -> getNameSql(s)).collect(Collectors.joining(",", "(", ")")));
        sb.append(" values ");
        sb.append(keys.stream().map(s -> "?").collect(Collectors.joining(",", "(", ")")));
        return sb.toString();
    }

    public static String getUpdateSql(List<String> keys, String tableName, String by) {
        StringBuilder sb = new StringBuilder();
        sb.append(" update ");
        sb.append(getNameSql(tableName));
        sb.append(" set ");
        sb.append(keys.stream().map(s -> getNameSql(s) + "=? ").collect(Collectors.joining(",")));
        sb.append(" where ");
        sb.append(getNameSql(by) + "=?");
        return sb.toString();
    }

    public static String getSelectSql(List<String> keys, String tableName, String by) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  ");
        if (keys == null || keys.isEmpty()) {
            sb.append("*");
        } else {
            sb.append(keys.stream().map(s -> getNameSql(s)).collect(Collectors.joining(",")));
        }
        sb.append(" from ");
        sb.append(getNameSql(tableName));
        sb.append(" where ");
        sb.append(getNameSql(by) + "=?");
        return sb.toString();
    }

    public static String getDeleteSql(String tableName, String by) {
        StringBuilder sb = new StringBuilder();
        sb.append(" delete  from ");
        sb.append(getNameSql(tableName));
        sb.append(" where ");
        sb.append(getNameSql(by) + "=?");
        return sb.toString();
    }

    public static String getSelectCountSql(String tableName, String by) {
        StringBuilder sb = new StringBuilder(getSelectCountSql(tableName));
        sb.append(" where ");
        sb.append(getNameSql(by) + "=?");
        return sb.toString();
    }

    public static String getSelectCountSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append(" select  count(*) as ");
        sb.append(getConstantSql("count"));
        sb.append(" from ");
        sb.append(getNameSql(tableName));
        return sb.toString();
    }

    public static String getInsertSql(String[] keys, String tableName) {
        return getInsertSql(Arrays.stream(keys).collect(Collectors.toList()), tableName);

    }

    public static String getUpdateSql(String[] keys, String tableName, String by) {
        return getUpdateSql(Arrays.stream(keys).collect(Collectors.toList()), tableName, by);
    }

    public static String getSelectSql(String[] keys, String tableName, String by) {
        return getSelectSql(Arrays.stream(keys).collect(Collectors.toList()), tableName, by);
    }

    public static String truncateSql(String tableName) {
        return "TRUNCATE TABLE " + getNameSql(tableName);
    }

    public static String createSql(String oldTable, String newTable) {
        return "CREATE TABLE " + getNameSql(newTable) + " SELECT * FROM " + getNameSql(oldTable) + " WHERE 1=2";
    }

    public static <T> T[] concat(T[] o1, T[] o2) {
        if (o1 == null) {
            return o2;
        }
        if (o2 == null) {
            return o1;
        }
        int l1 = o1.length, l2 = o2.length, l3 = l1 + l2;
        if (l1 == 0) {
            return o2;
        }
        if (l2 == 0) {
            return o1;
        }
        T[] array = (T[]) Array.newInstance(o1[0].getClass(), l3);
        for (int i = 0; i < l1; i++) {
            array[i] = o1[i];
        }
        for (int i = 0; i < l2; i++) {
            array[i + l1] = o2[i];
        }
        return array;
    }

    public static <T> T[] concat(T[]... ts) {
        Objects.requireNonNull(ts);
        int l = ts.length;
        if (l == 0) {
            return null;
        }
        if (l == 1) {
            return ts[0];
        }
        T[] array = ts[0];
        for (int i = 0; i < l - 1; i++) {
            array = concat(array, ts[i + 1]);
        }
        return array;
    }

}

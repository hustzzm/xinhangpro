package com.pig.basic.util;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

    public static String lowerFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] + 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    /**
     * 判断字符串中是否有字符百分号、逗号、下划线、斜杠
     * @param str
     * @return
     */
    public static Boolean hasSpecialWords(Object str) {
       String pattern = ".*[%._/].*";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(getCheckString(str));
        return matcher.matches();
    }

    /**
     * 把模糊查询的检索的关键字对特殊字符进行转换如“[”、“%”
     * @param object
     * 需要转换的字符串
     * @return 返回模糊查询的字符串
     */
    public static String ToLikeStr(Object object)
    {
        String str = getCheckString(object);
        if(str != null && str.length()>0)
        {
            str =str.trim().replace("\\", "\\\\%").replace("%", "\\%").replace("_", "\\_");
        }
        return str ;
// str =
    }

    public static boolean sqlValidate(String str) {
        str = str.toLowerCase();//统一转为小写
        String badStr = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
                "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
                "table|from|grant|use|group_concat|column_name|" +
                "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
                "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";//过滤掉的sql关键字，可以手动添加
        String[] badStrs = badStr.split("\\|");
        for (int i = 0; i < badStrs.length; i++) {
            if (str.indexOf(badStrs[i]) >= 0) {
                return true;
            }
        }
        return false;
    }

    public static String humpToUnderline(String para) {
        para = lowerFirst(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;

        for(int i = 0; i < para.length(); ++i) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                ++temp;
            }
        }
        return sb.toString().toLowerCase();
    }

    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        StringUtil task = new StringUtil();

//        task.doAutoTransFile();
//        task.doAutoUploadFileToDB();
        //String str = task.getStringToDate("2018/5/18");
        // System.out.println("str:" + str);
        boolean btest = hasSpecialWords("test1%3");
        System.out.println("btest:" +btest);
    }



    /**
     * 将字符串按照指定编码方式编码
     */
    public static String getStringToDate(String dateStr) {
        String s = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if(!("").equals(dateStr)){
                if(dateStr.contains("/")){
                    dateStr = dateStr.replaceAll("/","-");
                }
                s = format.format(format.parse(dateStr));
            }

        } catch (ParseException e) {
            System.out.println(e.getMessage());
        }
        return s;
    }

    /**
     * 将字符串按照指定编码方式编码
     *
     * @param source
     *            需要编码的字符串
     * @param oldEncode
     *            原始编码
     * @param newEncode
     *            新编码
     * @return 转码后的字符串
     */
    public static String encodeString(String source, String oldEncode,
                                      String newEncode) {
        if (isNull(source)) {
            return source;
        }
        try {
            String newString = new String(source.getBytes(oldEncode), newEncode);
            return newString;
        } catch (UnsupportedEncodingException e) {
            return source;
        }
    }

    /**
     * 将字符串按照指定编码方式编码，此方法的原始编码是iso-8859-1，新编码是utf-8
     *
     * @param source
     *            需要编码的字符串
     * @return 编码后的字符串
     */
    public static String encodeString(String source) {

        String oldEncode = "iso-8859-1";
        String newEncode = "utf-8";

        return encodeString(source, oldEncode, newEncode);
    }

    /**
     * 将字节流转化为UTF-8字符格式
     *
     * @param is
     *            字节流
     * @return 字符串格式的字节流
     */
    public static String getStringFromInputStream(InputStream is) {
        if (is == null) {
            return null;
        }
        String encode = "utf-8";
        String content = null;
        try {
            byte[] contentByte = new byte[is.available()];
            is.read(contentByte);
            content = new String(contentByte, encode);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("将InputStream转化成String失败!");
        }

        return content;
    }

    /**
     * 根据level级别,返回空格
     *
     * @param levelStr
     *            level级别
     * @return 对应的空格
     */
    public static String getBlankStrByLevel(String levelStr) {
        final String BLANK_HTML = "&nbsp;";
        StringBuffer sb = new StringBuffer();
        int level = Integer.parseInt(levelStr);
        for (int i = 0; i < level; i++) {
            sb.append(BLANK_HTML);
            sb.append(BLANK_HTML);
            sb.append(BLANK_HTML);
            sb.append(BLANK_HTML);
        }
        return sb.toString();
    }

    /**
     * 分隔字符串，根据指定的分隔符分割字符串
     *
     * @param str
     *            需要分割的字符串
     * @param split
     *            字符串的分隔符号
     * @return 返回分隔的字符
     */
    public static String[] splitString(String str, String split) {
        return str.split(split);
    }

    /**
     * 分隔字符串(默认分隔符为&)
     *
     * @param str
     *            需要分割的字符串
     * @return 返回分隔的字符
     */
    public static String[] splitString(String str) {

        // 默认使用的分割符号
        final String splitStirng = "&";
        return str.split(splitStirng);
    }

    /**
     * 得到对象对应的字符串结构
     *
     * @param o
     *            对象
     * @return 如果对象为NULL，则返回空字符串
     */
    public static String getStringFromObject(Object o) {

        if (o == null)
            return "";

        return o.toString();

    }

    /**
     * 判断字符是否是数字 true--是 false--否 ，0-9
     *
     * @param c
     *            字符
     * @return 如果是数字则返回true，如果不是则返回false
     */
    public static boolean isDigital(char c) {
        if (Character.isDigit(c)) {
            return true;
        }
        return false;
    }

    /**
     * deal bigdecimal
     * @param objValue 数据
     * @param digits 小数位数
     * @return
     */
    public static BigDecimal getCheckBigDecimal(Object objValue, int digits)
    {
        try {
            if(objValue == null ||objValue.toString().equals("") || objValue.toString().trim().equals(".")){
                return new BigDecimal(0);
            }else{
                return new BigDecimal(objValue.toString()).setScale(digits,BigDecimal.ROUND_HALF_UP);
            }
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    /**
     * deal bigdecimal
     * @param objValue 数据
     * @return
     */
    public static  Integer getCheckInteger(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new Integer(0);
            }else{
                return new Integer(objValue.toString());
            }
        } catch (Exception e) {
            return new Integer(0);
        }
    }

    /**
     * deal bigdecimal
     * @param objValue 数据
     * @return
     */
    public static  Integer getCheckNewInteger(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new Integer(-1);
            }else{
                return new Integer(objValue.toString());
            }
        } catch (Exception e) {
            return new Integer(-1);
        }
    }

    /**
     * deal Long
     * @param objValue 数据
     * @return
     */
    public static  Long getCheckLong(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new Long(0);
            }else{
                return new Long(objValue.toString());
            }
        } catch (Exception e) {
            return new Long(0);
        }
    }

    /**
     * deal bigdecimal
     * @param objValue 数据
     * @return
     */
    public static  Double getCheckDouble(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new Double(0);
            }else{
                return new Double(objValue.toString());
            }
        } catch (Exception e) {
            return new Double(0);
        }
    }

    /**
     * deal bigdecimal
     * @param objValue 数据
     * @return
     */
    public static String getCheckString(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return "";
            }else{
                return objValue.toString();
            }
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * deal bigdecimal
     * @param objValue 数据
     * @return
     */
    public static String getQueryString(String objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return "";
            }else{
                return objValue.trim().replaceAll(",","','")
                        .replaceAll(" ","','")
                        .replaceAll("\\t","','");
            }
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * add by hello 20200428
     * 回车换行\rz转换为html的BR
     * @param objValue 数据
     * @return
     */
    public static String getStringEnterToBr(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return "";
            }else{
                String val = objValue.toString();

                Pattern CRLF = Pattern.compile("(\\\\r\\\\n|\\\\r|\\\\n|\\\\n\\\\r)");
                Matcher m = CRLF.matcher(val);
                if (m.find()) {
                    val = m.replaceAll("<br/>");
                }
                return val;
            }
        } catch (Exception e) {
            return "";
        }
    }



    /**
     *
     * @param objValue 数据
     * @param digits 小数位数
     * @return
     */
    public static String setObjToBDDigitsStr(Object objValue,int digits)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new BigDecimal(0).toString();
            }else{
                return new BigDecimal(objValue.toString()).setScale(digits,BigDecimal.ROUND_HALF_UP).toString();
            }
        } catch (Exception e) {
            return new BigDecimal(0).toString();
        }
    }
    /**
     * 对数据，截取前两位小数，不四舍五入
     * @param objValue 数据
     * @param digits 小数位数
     * @return
     */
    public static String setObjToBDDigitsStrRounddown(Object objValue,int digits)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new BigDecimal(0).toString();
            }else{
                return new BigDecimal(objValue.toString()).setScale(digits,BigDecimal.ROUND_DOWN).toString();
            }
        } catch (Exception e) {
            return new BigDecimal(0).toString();
        }
    }

    /**
     * 对数据，截取前两位小数，不四舍五入
     * @param objValue 数据
     * @param &lt; &gt;&amp;&quot;&copy;分别是<，>，&，"，©;的转义字符
     * @return
     */
    public static String ConvertSpecialStr(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return "";
            }else{
                return objValue.toString().replaceAll("<","&lt")
                        .replaceAll(">","&gt")
                        .replaceAll("'","&nt");
            }
        } catch (Exception e) {
            return "";
        }
    }
    /**
     * 对数据，截取前两位小数，不四舍五入
     * @param objValue 数据
     * @param &lt; &gt;&amp;&quot;&copy;分别是<，>，&，"，©;的转义字符
     * @return
     */
    public static String ConvertBackSpecialStr(Object objValue)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return "";
            }else{
                return objValue.toString().replaceAll("&lt","<")
                        .replaceAll("&gt",">")
                        .replaceAll("&nt","'");
            }
        } catch (Exception e) {
            return "";
        }
    }

    public static BigDecimal setObjToBDDigitsBD(Object objValue,int digits)
    {
        try {
            if(objValue == null ||objValue.toString().equals("")){
                return new BigDecimal(0);
            }else{
                return new BigDecimal(objValue.toString()).setScale(digits,BigDecimal.ROUND_HALF_UP);
            }
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    /**
     * 去除字符串中的空格(全角和半角)
     *
     * @param str
     *            字符串
     * @return 去除空格后的字符串
     */
    public static String trim(String str) {
        return str.replaceAll("\\s", "").replaceAll("　", "");
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     *            对象
     * @return 如果为空则返回true，如果不为空则返回false
     */
    public static boolean isNull(Object o) {

        if (o != null && !o.toString().equals("")) {
            return false;
        }

        return true;
    }

    /**
     * 将CLOB转成字符串
     *
     * @param object
     *            需要转化的流对象
     * @return 转换后的字符串
     */
    public static String clobToString(Object object) {
        if (StringUtil.isNull(object)) {
            return "";
        }
        Reader reader = (Reader) object;
        BufferedReader br = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        String s = null;
        try {
            s = br.readLine();
            while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING
                sb.append(s);
                s = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String reString = sb.toString();
        return reString;
    }

    /**
     * 检查对象转换成数字
     *
     * @param value
     * @return
     */
    public static String checkObjForNum(Object value) {
        if (value == null || value.toString().trim().equals("")) {
            value = "0";
        }

        return value.toString().trim();
    }

    public static String checkObjForDb(Object value) {
        if (value == null || value.toString().trim().equals("")) {
            value = "0";
        }
        return new BigDecimal(value.toString().trim()).toPlainString();
    }

    public static BigDecimal checkObjForDb1(Object value) {
        if (value == null || value.toString().trim().equals("")) {
            value = new BigDecimal(0);
        }
        return new BigDecimal(value.toString().trim());
    }


    public static Long transObjToLong(Object value) {
        if (value == null || value.toString().trim().equals("")) {
            value = "0";
        }
        return Long.valueOf(value.toString().trim());
    }

    /**
     * 检查对象转换成字符
     *
     * @param value
     * @return
     */
    public static String checkObjForStr(Object value) {
        return checkObjForStr(value, "");
    }

    /**
     * 检查对象转换成字符
     *
     * @param value
     * @param emptyDefault
     * @return
     */
    public static String checkObjForStr(Object value,String emptyDefault) {
        if (value == null ) {
            return emptyDefault;
        }
        String tmp = value.toString().trim();
        if(tmp.equals("")) {
            return emptyDefault;
        }
        return tmp;
    }

    public static void printnewLine(String column, String msg) {
        System.out.println("-----------------" + column + ":" + msg
                + "-----------------");
    }

    // ，同时客串的位置是固定的
    public static String base = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // 生成字符串从此序列中取

    public static String getRandomCode(int length) { // length表示生成字符串的长度
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }


    public static String[] splitByToken(String input) {
        StringTokenizer tokenizer = new StringTokenizer(input);
        String[] arr = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreElements()) {
            arr[i] = tokenizer.nextToken();
            i++;
        }
        return arr;
    }
    public static String[] splitByToken(String input,String delim) {
        StringTokenizer tokenizer = new StringTokenizer(input,delim);
        String[] arr = new String[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreElements()) {
            arr[i] = tokenizer.nextToken();
            i++;
        }
        return arr;
    }

    /**
     * 把驼峰命名法 改成下划线命名法，表名转换
     * @param str
     * @return
     */
    public  static String humpToLine(String str){
        char[] arr = str.toCharArray();
        StringBuilder sb = new StringBuilder(arr.length+5);
        for(char c:arr) {
            if (c >= 65 && c <= 90) {
                sb.append("_"+(char)(c+32));
            }else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static String camelName(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    public static String camelNameClass(String name) {
        StringBuilder result = new StringBuilder();
        // 快速检查
        if (name == null || name.isEmpty()) {
            // 没必要转换
            return "";
        } else if (!name.contains("_")) {
            // 不含下划线，仅将首字母小写
            return name.substring(0, 1).toLowerCase() + name.substring(1);
        }
        // 用下划线将原始字符串分割
        String camels[] = name.split("_");
        for (String camel :  camels) {
            // 跳过原始字符串中开头、结尾的下换线或双重下划线
            if (camel.isEmpty()) {
                continue;
            }
            // 处理真正的驼峰片段
            if (result.length() == 0) {
                // 第一个驼峰片段，全部字母都小写
                result.append(camel.substring(0, 1).toUpperCase()+camel.substring(1).toLowerCase());
            } else {
                // 其他的驼峰片段，首字母大写
                result.append(camel.substring(0, 1).toUpperCase());
                result.append(camel.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

}

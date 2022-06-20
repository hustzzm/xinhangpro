package com.pig.basic.util;



import org.apache.commons.lang.time.DateUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @discrption:JDK8的时间工具类
 * @user:wengzhongjie
 * @createTime: 2021/1/3 14:30
 */
public class LocalDateUtils {

    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String PATTERN_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String PATTERN_YYYY_MM_DD_HH_MM_SS_SSS = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String PATTERN_HH_MM_SS = "HH:mm:ss";
    public static final String PATTERN_YYYYMMDD_SPLIT_BY_SLASH = "yyyy/MM/dd";
    public static final String PATTERN_YYYYMMDD_SPLIT_BY_POINT = "yyyy.MM.dd";
    public static final String PATTERN_YYYYMMDD_SPLIT_BY_CN = "yyyy年MM月dd日";
    public static final String PATTERN_YYYYMMDD_SPLIT_BY_CN_HH_MM = "yyyy年MM月dd日 HH:mm";

    /**
     * 春天;
     */
    public static final Integer SPRING = 1;

    /**
     * 夏天;
     */
    public static final Integer SUMMER = 2;

    /**
     * 秋天;
     */
    public static final Integer AUTUMN = 3;

    /**
     * 冬天;
     */
    public static final Integer WINTER = 4;

    /**
     * 星期日;
     */
    public static final String SUNDAY = "星期日";

    /**
     * 星期一;
     */
    public static final String MONDAY = "星期一";

    /**
     * 星期二;
     */
    public static final String TUESDAY = "星期二";

    /**
     * 星期三;
     */
    public static final String WEDNESDAY = "星期三";

    /**
     * 星期四;
     */
    public static final String THURSDAY = "星期四";

    /**
     * 星期五;
     */
    public static final String FRIDAY = "星期五";

    /**
     * 星期六;
     */
    public static final String SATURDAY = "星期六";

    /**
     * 年
     */
    private static final String YEAR = "year";

    /**
     * 月
     */
    private static final String MONTH = "month";

    /**
     * 周
     */
    private static final String WEEK = "week";

    /**
     * 日
     */
    private static final String DAY = "day";

    /**
     * 时
     */
    private static final String HOUR = "hour";

    /**
     * 分
     */
    private static final String MINUTE = "minute";

    /**
     * 秒
     */
    private static final String SECOND = "second";

    private static String[] weekOfDays = {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};


    /**
     * 存放不同日期模版格式的df的map
     */
    private static Map<String, DateTimeFormatter> DF = new HashMap();

    private static DateTimeFormatter getDf(final String pattern) {
        DateTimeFormatter dateTimeFormatter = DF.get(pattern);
        if (dateTimeFormatter == null) {
            synchronized (LocalDateUtils.class) {
                dateTimeFormatter = DF.get(pattern);
                if (dateTimeFormatter == null) {
                    dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
                }
            }
        }
        return dateTimeFormatter;
    }

    /**
     * 获取当前日期和时间字符串.
     *
     * @return String 日期时间字符串，例如 2015-08-11 09:51:53
     */
    public static String getLocalDateTimeStr() {
        return format(LocalDateTime.now(), PATTERN_YYYY_MM_DD_HH_MM_SS);
    }


    /**
     * 获取当前日期字符串.
     *
     * @return String 日期字符串，例如2015-08-11
     */
    public static String getLocalDateStr() {
        return format(LocalDate.now(), PATTERN_YYYY_MM_DD);
    }

    /**
     * 获取当前时间字符串.
     *
     * @return String 时间字符串，例如 09:51:53
     */
    public static String getLocalTimeStr() {
        return format(LocalTime.now(), PATTERN_HH_MM_SS);
    }

    /**
     * 根据ChronoUnit计算两个日期时间之间相隔日期时间
     *
     * @param start      开始日期时间
     * @param end        结束日期时间
     * @param chronoUnit 日期时间单位
     * @return long 相隔日期时间
     */
    public static long getChronoUnitBetween(LocalDateTime start, LocalDateTime end, ChronoUnit chronoUnit) {
        return Math.abs(start.until(end, chronoUnit));
    }

    /**
     * 根据ChronoUnit计算两个日期时间之间相隔日期时间
     * @param start
     * @param end
     * @param chronoUnit
     * @return
     */
    public static long getChronoUnitBetween(Date start,Date end,ChronoUnit chronoUnit){
        LocalDateTime startTime = conversionDate2LocalDateTime(start);
        LocalDateTime endTime =conversionDate2LocalDateTime(end);
        return getChronoUnitBetween(startTime, endTime,chronoUnit);
    }

    /**
     * 根据ChronoUnit计算两个日期之间相隔年数或月数或天数
     *
     * @param start      开始日期
     * @param end        结束日期
     * @param chronoUnit 日期时间单位,(ChronoUnit.YEARS,ChronoUnit.MONTHS,ChronoUnit.WEEKS,ChronoUnit.DAYS)
     * @return long 相隔年数或月数或天数
     */
    public static long getChronoUnitBetween(LocalDate start, LocalDate end, ChronoUnit chronoUnit) {
        return Math.abs(start.until(end, chronoUnit));
    }



    /**
     * 获取当前星期字符串.
     *
     * @return String 当前星期字符串，例如 星期二
     */
    public static String getDayOfWeekStr() {
        return format(LocalDate.now(), "E");
    }

    /**
     * 获取指定日期是星期几
     *
     * @param localDate 日期
     * @return String 星期几
     */
    public static String getDayOfWeekStr(LocalDate localDate) {
        int dayOfWeek = localDate.getDayOfWeek().getValue() - 1;
        return weekOfDays[dayOfWeek];
    }

    /**
     * 获取本年第一天的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfYearStr() {
        return getFirstDayOfYearStr(LocalDateTime.now());
    }

    /**
     * 获取本年最后一天的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfYearStr() {
        return getLastDayOfYearStr(LocalDateTime.now());
    }

    /**
     * 获取指定日期当年第一天的日期字符串
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfYearStr(LocalDateTime localDateTime) {
        return getFirstDayOfYearStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期当年最后一天的日期字符串
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfYearStr(LocalDateTime localDateTime) {
        return getLastDayOfYearStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期当年第一天的日期字符串,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfYearStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.withDayOfYear(1).withHour(0).withMinute(0).withSecond(0), pattern);
    }

    /**
     * 获取指定日期当年最后一天的日期字符串,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfYearStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59), pattern);
    }

    /**
     * 获取本月第一天的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfMonthStr() {
        return getFirstDayOfMonthStr(LocalDateTime.now());
    }

    /**
     * 获取本月最后一天的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfMonthStr() {
        return getLastDayOfMonthStr(LocalDateTime.now());
    }

    /**
     * 获取指定日期当月第一天的日期字符串
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getFirstDayOfMonthStr(LocalDateTime localDateTime) {
        return getFirstDayOfMonthStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期当月最后一天的日期字符串
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfMonthStr(LocalDateTime localDateTime) {
        return getLastDayOfMonthStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期当月第一天的日期字符串,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfMonthStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0), pattern);
    }

    /**
     * 获取指定日期当月最后一天的日期字符串,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfMonthStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59), pattern);
    }

    /**
     * 获取本周第一天的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfWeekStr() {
        return getFirstDayOfWeekStr(LocalDateTime.now());
    }

    /**
     * 获取本周最后一天的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfWeekStr() {
        return getLastDayOfWeekStr(LocalDateTime.now());
    }

    /**
     * 获取指定日期当周第一天的日期字符串,这里第一天为周一
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfWeekStr(LocalDateTime localDateTime) {
        return getFirstDayOfWeekStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期当周最后一天的日期字符串,这里最后一天为周日
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfWeekStr(LocalDateTime localDateTime) {
        return getLastDayOfWeekStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期当周第一天的日期字符串,这里第一天为周一,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getFirstDayOfWeekStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0), pattern);
    }

    /**
     * 获取指定日期当周最后一天的日期字符串,这里最后一天为周日,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getLastDayOfWeekStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59), pattern);
    }

    /**
     * 获取今天开始时间的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getStartTimeOfDayStr() {
        return getStartTimeOfDayStr(LocalDateTime.now());
    }

    /**
     * 获取今天结束时间的日期字符串
     *
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getEndTimeOfDayStr() {
        return getEndTimeOfDayStr(LocalDateTime.now());
    }

    /**
     * 获取指定日期开始时间的日期字符串
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 00:00:00
     */
    public static String getStartTimeOfDayStr(LocalDateTime localDateTime) {
        return getStartTimeOfDayStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期结束时间的日期字符串
     *
     * @param localDateTime 指定日期时间
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getEndTimeOfDayStr(LocalDateTime localDateTime) {
        return getEndTimeOfDayStr(localDateTime, PATTERN_YYYY_MM_DD_HH_MM_SS);
    }

    /**
     * 获取指定日期开始时间的日期字符串,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd HH:mm:ss
     */
    public static String getStartTimeOfDayStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.withHour(0).withMinute(0).withSecond(0), pattern);
    }

    /**
     * 获取指定日期结束时间的日期字符串,带日期格式化参数
     *
     * @param localDateTime 指定日期时间
     * @param pattern       日期时间格式
     * @return String 格式：yyyy-MM-dd 23:59:59
     */
    public static String getEndTimeOfDayStr(LocalDateTime localDateTime, String pattern) {
        return format(localDateTime.withHour(23).withMinute(59).withSecond(59), pattern);
    }

    /**
     * 切割日期。按照周期切割成小段日期段。例如： <br>
     *
     * @param startDate 开始日期（yyyy-MM-dd）
     * @param endDate   结束日期（yyyy-MM-dd）
     * @param period    周期（天，周，月，年）
     * @return 切割之后的日期集合
     * <li>startDate="2019-02-28",endDate="2019-03-05",period="day"</li>
     * <li>结果为：[2019-02-28, 2019-03-01, 2019-03-02, 2019-03-03, 2019-03-04, 2019-03-05]</li><br>
     * <li>startDate="2019-02-28",endDate="2019-03-25",period="week"</li>
     * <li>结果为：[2019-02-28,2019-03-06, 2019-03-07,2019-03-13, 2019-03-14,2019-03-20,
     * 2019-03-21,2019-03-25]</li><br>
     * <li>startDate="2019-02-28",endDate="2019-05-25",period="month"</li>
     * <li>结果为：[2019-02-28,2019-02-28, 2019-03-01,2019-03-31, 2019-04-01,2019-04-30,
     * 2019-05-01,2019-05-25]</li><br>
     * <li>startDate="2019-02-28",endDate="2020-05-25",period="year"</li>
     * <li>结果为：[2019-02-28,2019-12-31, 2020-01-01,2020-05-25]</li><br>
     */
    public static List<String> listDateStrs(String startDate, String endDate, String period) {
        List<String> result = new ArrayList<>();
        DateTimeFormatter dateTimeFormatter = getDf(PATTERN_YYYY_MM_DD);
        LocalDate end = LocalDate.parse(endDate, dateTimeFormatter);
        LocalDate start = LocalDate.parse(startDate, dateTimeFormatter);
        LocalDate tmp = start;
        switch (period) {
            case DAY:
                while (start.isBefore(end) || start.isEqual(end)) {
                    result.add(start.toString());
                    start = start.plusDays(1);
                }
                break;
            case WEEK:
                while (tmp.isBefore(end) || tmp.isEqual(end)) {
                    if (tmp.plusDays(6).isAfter(end)) {
                        result.add(tmp.toString() + "," + end);
                    } else {
                        result.add(tmp.toString() + "," + tmp.plusDays(6));
                    }
                    tmp = tmp.plusDays(7);
                }
                break;
            case MONTH:
                while (tmp.isBefore(end) || tmp.isEqual(end)) {
                    LocalDate lastDayOfMonth = tmp.with(TemporalAdjusters.lastDayOfMonth());
                    if (lastDayOfMonth.isAfter(end)) {
                        result.add(tmp.toString() + "," + end);
                    } else {
                        result.add(tmp.toString() + "," + lastDayOfMonth);
                    }
                    tmp = lastDayOfMonth.plusDays(1);
                }
                break;
            case YEAR:
                while (tmp.isBefore(end) || tmp.isEqual(end)) {
                    LocalDate lastDayOfYear = tmp.with(TemporalAdjusters.lastDayOfYear());
                    if (lastDayOfYear.isAfter(end)) {
                        result.add(tmp.toString() + "," + end);
                    } else {
                        result.add(tmp.toString() + "," + lastDayOfYear);
                    }
                    tmp = lastDayOfYear.plusDays(1);
                }
                break;
            default:
                break;
        }
        return result;
    }



    /**
     * 获取指定日期时间加上指定数量日期时间单位之后的日期时间.
     *
     * @param localDateTime 日期时间
     * @param num           数量
     * @param chronoUnit    日期时间单位
     * @return LocalDateTime 新的日期时间
     */
    public static LocalDateTime plus(LocalDateTime localDateTime, int num, ChronoUnit chronoUnit) {
        return localDateTime.plus(num, chronoUnit);
    }

    /**
     * 获取指定日期时间减去指定数量日期时间单位之后的日期时间.
     *
     * @param localDateTime 日期时间
     * @param num           数量
     * @param chronoUnit    日期时间单位
     * @return LocalDateTime 新的日期时间
     */
    public static LocalDateTime minus(LocalDateTime localDateTime, int num, ChronoUnit chronoUnit) {
        return localDateTime.minus(num, chronoUnit);
    }

    /**
     * 时间四舍五入
     *
     * @param date  时间
     * @param field 四舍五入的时间字段
     * @return 例如: 2021-1-3 15:25:12.567 按秒四舍五入 2021-1-3 15:25:13
     * 应用场景:在存入某些数据库(如mysql)会自动加1秒存入表中,此方法可以提供一些思路
     */
    public static Date round(Date date, int field) {
        return DateUtils.round(date, field);
    }

    /**
     * 时间四舍五入  默认按秒
     *
     * @param date 时间
     * @return
     */
    public static Date round(Date date) {
        return round(date, Calendar.SECOND);
    }

    /**
     * @param start 第一个时间
     * @param end   第二个时间
     * @param round 是否四舍五入 true的话将进行毫秒值四舍五入(round方法)否则不进行。
     * @return
     */
    public static boolean isSameSecond(Date start, Date end, boolean round) {
        if (round) {
            start = round(start);
            end = round(end);
        }
        LocalDateTime startDateTime = conversionDate2LocalDateTime(start);
        LocalDateTime endDateTime = conversionDate2LocalDateTime(end);
        return format(startDateTime, PATTERN_YYYYMMDDHHMMSS).equalsIgnoreCase(format(endDateTime,PATTERN_YYYYMMDDHHMMSS));
    }

    /**
     * 判断是否是同一秒，对时间进行四舍五入
     * @param start 第一个时间
     * @param end   第二个时间
     * @return
     */
    public static boolean isSameSecond(Date start, Date end) {
        return isSameSecond(start, end, true);
    }


    /**
     * 格式化方法
     *
     * @param temporal 需要转化的日期时间
     * @param pattern  时间格式
     * @return String 日期时间字符串，例如 2015-08-11 09:51:53
     */
    public static String format(TemporalAccessor temporal, String pattern) {
        DateTimeFormatter dateTimeFormatter = getDf(pattern);
        return dateTimeFormatter.format(temporal);
    }

    /**
     * 日期时间字符串转换为日期时间(java.time.LocalDateTime)
     *
     * @param localDateTimeStr 日期时间字符串
     * @param pattern          日期时间格式 例如DATETIME_PATTERN
     * @return LocalDateTime 日期时间
     */
    public static LocalDateTime parseLocalDateTime(String localDateTimeStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = getDf(pattern);
        return LocalDateTime.parse(localDateTimeStr, dateTimeFormatter);
    }

    /**
     * 日期字符串转换为日期(java.time.LocalDate)
     *
     * @param localDateStr 日期字符串
     * @param pattern      日期格式 例如DATE_PATTERN
     * @return LocalDate 日期
     */
    public static LocalDate parseLocalDate(String localDateStr, String pattern) {
        DateTimeFormatter dateTimeFormatter = getDf(pattern);
        return LocalDate.parse(localDateStr, dateTimeFormatter);
    }


    /**
     * 功能描述: 将日期类Date 转换成LocalDateTime
     *
     * @param date
     * @return LocalDateTime 日期时间
     */
    public static LocalDateTime conversionDate2LocalDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    /**
     * 合法校验
     * @param pattern
     * @param value
     * @return
     */
    public static boolean isValid(String pattern,String value){
        if (value == null || value.length() == 0) {
            return false;
        }
        if (pattern.length() != value.length()) {
            return false;
        }
        try{
            getDf(pattern).parse(value);
        }catch (Exception e){
            return false;
        }
        return true;
    }



}

package com.pig.basic.util.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class DateUtils {

    public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE = "yyyy-MM-dd";
    public static final String DATE_TIME_MILLI = "yyyy-MM-dd HH:mm:ss:SSS";
    public static final String DATE_YYMMDDHHMM = "yyyyMMddHHmm";
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME);
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(DATE);
    public static final DateTimeFormatter DATE_TIME_MILLI_FORMAT = DateTimeFormatter.ofPattern(DATE_TIME_MILLI);
    public static final DateTimeFormatter DATE_YYMMDDHHMM_FORMAT = DateTimeFormatter.ofPattern(DATE_YYMMDDHHMM);

    public static final Map<String, DateTimeFormatter> datetimeFormatterMap = new HashMap() {{
        put(DATE_TIME, DATE_TIME_FORMAT);
        put(DATE, DATE_FORMAT);
        put(DATE_TIME_MILLI, DATE_TIME_MILLI_FORMAT);
        put(DATE_YYMMDDHHMM, DATE_YYMMDDHHMM_FORMAT);
    }};


    public static LocalDateTime strToTime(String str, String format) {
        return LocalDateTime.parse(str, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDateTime strToTime(String str) {
        return LocalDateTime.parse(str, DATE_TIME_FORMAT);
    }

    public static LocalDateTime strToTime2(String str) {
        return LocalDateTime.parse(str);
    }

    public static LocalDate strToDate(String str, String format) {
        return LocalDate.parse(str, DateTimeFormatter.ofPattern(format));
    }

    public static LocalDate strToDate(String str) {
        return LocalDate.parse(str, DATE_FORMAT);
    }

    public static LocalDate strToDate2(String str) {
        return LocalDate.parse(str);
    }

    public static String toString(LocalDate date) {
        return DATE_FORMAT.format(date);
    }

    public static String toString(LocalDateTime time) {
        return DATE_TIME_FORMAT.format(time);
    }

    public static String toString(Date l) {
        return toString(toLocalDateTime(l));
    }

    public static String toString(Instant l) {
        return toString(toLocalDateTime(l));
    }

    public static String toString(long l) {
        return toString(toLocalDateTime(l));
    }


    public static String toString(LocalDate date,String str) {
        return DateTimeFormatter.ofPattern(str).format(date);
    }

    public static String toString(LocalDateTime time,String str) {
        return DateTimeFormatter.ofPattern(str).format(time);
    }

    public static Instant toInstant(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant();//ZoneOffset.ofHours(8);
    }

    public static Instant toInstant(LocalDateTime time) {
        return time.atZone(ZoneId.systemDefault()).toInstant();
    }

    public static Instant toInstant(Date date) {
        return date.toInstant();
    }

    public static Instant toInstant(long timestamp) {
        return Instant.ofEpochMilli(timestamp);
    }

    public static Date toDate(LocalDate date) {
        return Date.from(toInstant(date));
    }

    public static Date toDate(LocalDateTime time) {

        return Date.from(toInstant(time));
    }

    public static Date toDate(Instant instant) {

        return Date.from(instant);
    }

    public static Date toDate(long l) {

        return new Date(l);
    }


    public static long toLong(LocalDate date) {
        return toInstant(date).toEpochMilli();
    }

    public static long toLong(LocalDateTime time) {
        return toInstant(time).toEpochMilli();
    }

    public static long toLong(Date date) {
        return date.getTime();
    }

    public static long toLong(Instant instant) {
        return instant.toEpochMilli();
    }


    public static LocalDate toLocalDate(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDate toLocalDate(long l) {
        return toLocalDate(toInstant(l));
    }

    public static LocalDate toLocalDate(Date l) {
        return toLocalDate(toInstant(l));
    }

    public static LocalDate toLocalDate(LocalDateTime l) {
        return toLocalDate(toInstant(l));
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDateTime toLocalDateTime(long l) {
        return toLocalDateTime(toInstant(l));
    }

    public static LocalDateTime toLocalDateTime(Date l) {
        return toLocalDateTime(toInstant(l));
    }

    public static LocalDateTime toLocalDateTime(LocalDate l) {
        return toLocalDateTime(toInstant(l));
    }


    public static String minus8Hours(Date d) {
        return toString(toLocalDateTime(d).minusHours(8));
    }

    public static String format(Date date,String pattern){
        return datetimeFormatterMap.get(pattern).format(toLocalDateTime(date));
    }

}

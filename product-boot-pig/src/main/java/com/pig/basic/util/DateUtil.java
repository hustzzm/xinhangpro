package com.pig.basic.util;

import com.baomidou.mybatisplus.extension.exceptions.ApiException;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.Assert;

import java.lang.ref.SoftReference;
import java.nio.file.FileSystems;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
public final class DateUtil {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd";
    public static final String FULL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String ULTIMATE_PATTERN = "EEE, yyyy-MM-dd HH:mm:ss zzz";
    private static final String[] DEFAULT_PATTERNS = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "EEE, yyyy-MM-dd HH:mm:ss zzz"};
    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
    public static final TimeZone GMT8 = TimeZone.getTimeZone("GMT+8");

    protected static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    protected static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式

    private DateUtil() {
    }

    public static Date parseDate(String dateValue) {
        return parseDate(dateValue, (String[])null, (Date)null);
    }

    public static Date parseDate(String dateValue, String... dateFormats) {
        return parseDate(dateValue, dateFormats, (Date)null);
    }

    public static Date parseDate(String dateValue, String[] dateFormats, Date startDate) {
        Assert.notNull(dateValue, "Date value may not be null");
        String[] localDateFormats = dateFormats != null ? dateFormats : DEFAULT_PATTERNS;
        Date localStartDate = startDate != null ? startDate : DEFAULT_TWO_DIGIT_YEAR_START;
        String v = dateValue;
        if (dateValue.length() > 1 && dateValue.startsWith("'") && dateValue.endsWith("'")) {
            v = dateValue.substring(1, dateValue.length() - 1);
        }

        String[] var6 = localDateFormats;
        int var7 = localDateFormats.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String dateFormat = var6[var8];
            SimpleDateFormat dateParser = DateFormatHolder.formatFor(dateFormat);
            dateParser.set2DigitYearStart(localStartDate);
            ParsePosition pos = new ParsePosition(0);
            Date result = dateParser.parse(v, pos);
            if (pos.getIndex() != 0) {
                return result;
            }
        }

        return null;
    }

    public static Date formatDate(Long millis) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(millis);
        return instance.getTime();
    }

    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd");
    }

    public static String formatDate(Date date, String pattern) {
        Assert.notNull(date, "Date may not be null");
        Assert.notNull(pattern, "Pattern may not be null");
        SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
        return formatter.format(date);
    }

    public static String formatString(String date, String srcPattern) {
        return formatString(date, srcPattern, "yyyy-MM-dd");
    }

    public static String formatString(String date, String srcPattern, String pattern) {
        Assert.notNull(date, "String may not be null");
        Assert.notNull(pattern, "Pattern may not be null");
        SimpleDateFormat parseer = DateFormatHolder.formatFor(srcPattern);

        try {
            Date parse = parseer.parse(date);
            SimpleDateFormat formatter = DateFormatHolder.formatFor(pattern);
            return formatter.format(parse);
        } catch (ParseException var6) {
            var6.printStackTrace();
            log.error(var6);
            throw new ApiException("时间格式[" + date + "]错误！");
        }
    }

    public static void clearThreadLocal() {
        DateFormatHolder.clearThreadLocal();
    }

    public static String buildDateDirectory(Date date) {
        String shortDate = formatDate(date);
        String[] shortDateArr = shortDate.split("-");
        String separator = FileSystems.getDefault().getSeparator();
        StringBuilder sb = new StringBuilder();
        sb.append(shortDateArr[0]).append(separator).append(shortDateArr[1]).append(separator).append(shortDateArr[2]).append(separator);
        return sb.toString();
    }

    public static void main(String[] args) {

        String timeStr = "1650551622211";
        String newStr = df.format(formatDate(Long.parseLong(timeStr)));
        System.out.println("newStr: " + newStr);

        Date dateNow = new Date();
        try {
            Date testDate = sdf.parse("2022-08-12");
            int daycount = daysOfTwo(dateNow,testDate);
            System.out.println("tttt:" + daycount);
        } catch (Exception ex){

        }

    }
    public static String getNowDate() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 判断两个日期相差的天数
     * @param fDate
     * @param oDate
     * @return
     */
    public static int daysOfTwo(Date fDate, Date oDate) {

        if (null == fDate || null == oDate) {

            return -1;

        }

        long intervalMilli = oDate.getTime() - fDate.getTime();

        return (int) (intervalMilli / (24 * 60 * 60 * 1000));

    }

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(GMT8);
        calendar.set(2000, 0, 1, 0, 0, 0);
        calendar.set(14, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = calendar.getTime();
    }

    static final class DateFormatHolder {
        private static final ThreadLocal<SoftReference<Map<String, SimpleDateFormat>>> THREADLOCAL_FORMATS = new ThreadLocal();

        DateFormatHolder() {
        }

        public static SimpleDateFormat formatFor(String pattern) {
            SoftReference<Map<String, SimpleDateFormat>> ref = (SoftReference)THREADLOCAL_FORMATS.get();
            Map<String, SimpleDateFormat> formats = ref == null ? null : (Map)ref.get();
            if (formats == null) {
                formats = new HashMap();
                THREADLOCAL_FORMATS.set(new SoftReference(formats));
            }

            SimpleDateFormat format = (SimpleDateFormat)((Map)formats).get(pattern);
            if (format == null) {
                new SimpleDateFormat(pattern);
                format = new SimpleDateFormat(pattern, Locale.CHINA);
                format.setTimeZone(DateUtil.GMT8);
                ((Map)formats).put(pattern, format);
            }

            return format;
        }

        public static void clearThreadLocal() {
            THREADLOCAL_FORMATS.remove();
        }
    }
}

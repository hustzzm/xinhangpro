package com.pig.basic.util;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;

/**
 * @author lidd
 * @date 2022/6/22 10:44
 */
public class CheckCommon {

    /**
     * 订单类别头
     */
    private static final String ORDER_CODE = "O";
    /**
     * 退货类别头
     */
    private static final String RETURN_ORDER = "2";
    /**
     * 退款类别头
     */
    private static final String REFUND_ORDER = "3";
    /**
     * 未付款重新支付别头
     */
    private static final String AGAIN_ORDER = "4";
    /**
     * 随即编码
     */
    private static final int[] r = new int[]{7, 9, 6, 2, 8, 1, 3, 0, 5, 4};
    /**
     * 用户id和随机数总长度
     */
    private static final int maxLength = 14;

    /**
     * 生成编号
     *
     * @param type 类型
     * @return
     */
    public static String newCheckId(String type) {
        //C + 当前日期时间(2022-05-09 16:40:32)时间戳转36位 + 3位随机数
        //例如：CBieNMJO8h  C+BieNMJ+O8h
        StringBuilder sb = new StringBuilder(type);
        sb.append(Long.toString(System.currentTimeMillis() / 1000, 36))
                .append(StringUtil.getRandomCode(3));
        return sb.toString();
    }

    public static String buildAge(Integer ageYear, Integer ageMonth, Integer ageDay) {
        String ageYearStr = "";
        String ageMonthStr = "";
        String ageDayStr = "";
        if (ageYear != null) {
            ageYearStr = ageYear.toString() + "岁";
        }
        if (ageMonth != null) {
            ageMonthStr = ageMonth.toString() + "月";
        }
        if (ageDay != null) {
            ageDayStr = ageDay.toString() + "天";
        }
        return ageYearStr + ageMonthStr + ageDayStr;
    }


    //生成解读编号
    public static String newExplainId() {
        //E + 当前日期时间(2022-05-09 16:40:32)时间戳转36位 + 3位随机数
        //例如：CBieNMJO8h  C+BieNMJ+O8h
        StringBuilder sb = new StringBuilder("E");
        sb.append(Long.toString(System.currentTimeMillis() / 1000, 36))
                .append(StringUtil.getRandomCode(3));
        return sb.toString();
    }

    /**
     * 检查参数是否为空
     *
     * @param params params
     * @return String
     */
    public static String checkParams(Map<String, Object> params) {
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (StringUtil.isNull(entry.getValue())) {
                return entry.getKey() + "不能为空！";
            }
        }
        return "";
    }
    
    /**
     * 生成订单单号编码
     */
    public static String getOrderCode() {
        return ORDER_CODE + getCode();
    }


    /**
     * 生成退货单号编码
     */
    public static String getReturnCode() {
        return RETURN_ORDER + getCode();
    }


    /**
     * 生成退款单号编码
     */
    public static String getRefundCode() {
        return REFUND_ORDER + getCode();
    }

    /**
     * 未付款重新支付
     */
    public static String getAgainCode() {
        return AGAIN_ORDER + getCode();
    }


    /**
     * 更具id进行加密+加随机数组成固定长度编码
     */
    private static String toCode(Long id) {
        String idStr = id.toString();
        StringBuilder idsbs = new StringBuilder();
        for (int i = idStr.length() - 1; i >= 0; i--) {
            idsbs.append(r[idStr.charAt(i) - '0']);
        }
        return idsbs.append(getRandom(maxLength - idStr.length())).toString();
    }

    /**
     * 生成时间戳
     */
    private static String getDateTime() {
        DateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    /**
     * 生成固定长度随机码
     *
     * @param n 长度
     */
    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        long rangeLong = (((long) (new Random().nextDouble() * (max - min)))) + min;
        return rangeLong;
    }

    /**
     * 生成不带类别标头的编码
     */
    private static synchronized String getCode() {
        return getDateTime() + toCode(10000l);
    }
}
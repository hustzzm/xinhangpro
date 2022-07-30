package com.pig.modules.core;

import com.pig.basic.util.StringUtil;

public class BusinessUtil {

    public static String getBookTimeText(String bookTimes){

        String result = "";
        if(StringUtil.isNull(bookTimes)){
            return result;
        }

        String[] arr = bookTimes.split(",");
        if(arr.length == 1){
            int itmpval = StringUtil.getCheckInteger(bookTimes);

            result = String.valueOf(itmpval) + ":00-" + String.valueOf(itmpval+1) + ":00";
        }else{
            int itmpval = StringUtil.getCheckInteger(arr[0]);
            int itmpval2 = StringUtil.getCheckInteger(arr[arr.length -1]);

            result = String.valueOf(itmpval) + ":00-" + String.valueOf(itmpval2 + 1) + ":00";
        }

        return result;
    }
}

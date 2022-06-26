package com.pig.basic.adapter;

import cn.hutool.core.util.StrUtil;
import com.pig.basic.util.DateUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期适配器
 * @author Chris Wang
 */
public class DateAdapter2 extends TypeAdapter<Date> {


    @Override
    public void write(JsonWriter jsonWriter, Date date) throws IOException {
        jsonWriter.value(DateUtil.formatDate(date));
    }

    @Override
    public Date read(JsonReader jsonReader) throws IOException {
        if(jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        final String s = jsonReader.nextString();
        return DateUtil.parseDate(s);
    }

}
